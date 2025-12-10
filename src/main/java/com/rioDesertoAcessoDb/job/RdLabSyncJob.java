package com.rioDesertoAcessoDb.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;
import java.util.Date;

@Component
public class RdLabSyncJob {

    @Autowired
    private JdbcTemplate postgresJdbcTemplate;

    private static final List<String> TABELAS_ORDENADAS = Arrays.asList(
            "IDENTIFICACAO",
            "ANALISE",
            "AMOSTRA_QUIMICO",
            "AMOSTRAANALISE_QUIMICO"
    );

    private static final String FIREBIRD_URL = "jdbc:firebirdsql://192.168.108.88:3050//dataib/rdlab.fdb";
    private static final String FIREBIRD_USER = "ALCATEIA";
    private static final String FIREBIRD_PASSWORD = "8D5Z9s2F";

    @Scheduled(cron = "0 0 6 * * MON-FRI", zone = "America/Sao_Paulo")
//    @Scheduled(fixedDelay = 60000, initialDelay = 5000)
    public void sincronizarDadosRdLab() {
        System.out.println("=== SINCRONIZAÇÃO DE DADOS RD LAB ===");
        System.out.println("Data/Hora: " + new Date());

        long inicioTotal = System.currentTimeMillis();

        System.out.println("\n1. Testando PostgreSQL...");
        long inicioPostgres = System.currentTimeMillis();
        try {
            String pgVersion = postgresJdbcTemplate.queryForObject("SELECT version()", String.class);
            long fimPostgres = System.currentTimeMillis();
            System.out.println("PostgreSQL OK: " + pgVersion.split(",")[0]);
            System.out.println("Tempo conexão: " + (fimPostgres - inicioPostgres) + "ms");
        } catch (Exception e) {
            System.err.println(" PostgreSQL FALHOU: " + e.getMessage());
            return;
        }

        System.out.println("\n2. Testando Firebird...");
        long inicioFirebird = System.currentTimeMillis();
        try (Connection fbConn = DriverManager.getConnection(FIREBIRD_URL, FIREBIRD_USER, FIREBIRD_PASSWORD)) {
            long fimFirebird = System.currentTimeMillis();
            String fbProduct = fbConn.getMetaData().getDatabaseProductName();
            String fbVersion = fbConn.getMetaData().getDatabaseProductVersion();
            System.out.println("Firebird OK: " + fbProduct + " - " + fbVersion);
            System.out.println("Tempo conexão: " + (fimFirebird - inicioFirebird) + "ms");

            System.out.println("\n3. Iniciando sincronização de tabelas...");
            long inicioSincronizacaoTotal = System.currentTimeMillis();
            sincronizarTodasTabelasOrdenadas(fbConn);
            long fimSincronizacaoTotal = System.currentTimeMillis();
            System.out.println("Tempo total sincronização: " + (fimSincronizacaoTotal - inicioSincronizacaoTotal) + "ms");

        } catch (Exception e) {
            System.err.println("Firebird FALHOU: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        long fimTotal = System.currentTimeMillis();
        System.out.println("\n=== SINCORNIZAÇÃO CONCLUÍDA ===");
        System.out.println("Tempo total: " + (fimTotal - inicioTotal) + "ms");
        System.out.println("================================\n");
    }

    private void sincronizarTodasTabelasOrdenadas(Connection firebirdConn) {
        long inicioTotal = System.currentTimeMillis();
        int tabelasProcessadas = 0;
        int tabelasComErro = 0;
        int totalRegistros = 0;

        for (String tabelaFirebird : TABELAS_ORDENADAS) {
            String tabelaPostgres = tabelaFirebird.toLowerCase();

            // Verificar se a tabela existe no Firebird
            if (!tabelaExisteNoFirebird(firebirdConn, tabelaFirebird)) {
                System.out.println("\n[PULANDO] Tabela " + tabelaFirebird + " não encontrada no Firebird");
                continue;
            }

            System.out.println("\n" + tabelaFirebird + ":");
            try {
                // Verificar se tabela existe no PostgreSQL
                if (!tabelaExisteNoPostgres(tabelaPostgres)) {
                    System.err.println("   ✗ Tabela não existe no PostgreSQL! Crie manualmente antes de sincronizar.");
                    continue;
                }

                // Sincronizar dados
                int registros = sincronizarDadosTabela(firebirdConn, tabelaFirebird, tabelaPostgres);
                totalRegistros += registros;
                tabelasProcessadas++;

            } catch (Exception e) {
                tabelasComErro++;
                System.err.println("   ✗ ERRO: " + e.getMessage());

                // Se for tabela importante, podemos interromper
                if (tabelaFirebird.equals("IDENTIFICACAO") || tabelaFirebird.equals("ANALISE")) {
                    System.err.println("   Tabela crítica com erro, abortando sincronização!");
                    break;
                }
            }
        }

        long fimTotal = System.currentTimeMillis();
        System.out.println("\n" + "=".repeat(50));
        System.out.println("RESUMO FINAL:");
        System.out.println("   Tabelas processadas: " + tabelasProcessadas + "/" + TABELAS_ORDENADAS.size());
        System.out.println("   Tabelas com erro: " + tabelasComErro);
        System.out.println("   Total de registros: " + totalRegistros);
        System.out.println("   Tempo total: " + (fimTotal - inicioTotal) + "ms");
        System.out.println("=".repeat(50));
    }

    private boolean tabelaExisteNoFirebird(Connection conn, String tabela) {
        try {
            String query = "SELECT 1 FROM RDB$RELATIONS WHERE RDB$RELATION_NAME = ? AND RDB$SYSTEM_FLAG = 0";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, tabela.toUpperCase());
                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (Exception e) {
            System.err.println("   Erro ao verificar tabela " + tabela + " no Firebird: " + e.getMessage());
            return false;
        }
    }

    private boolean tabelaExisteNoPostgres(String tabela) {
        try {
            String query = "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = ?)";
            return Boolean.TRUE.equals(postgresJdbcTemplate.queryForObject(query, Boolean.class, tabela));
        } catch (Exception e) {
            return false;
        }
    }

    private int sincronizarDadosTabela(Connection firebirdConn, String tabelaFirebird, String tabelaPostgres) throws SQLException {
        long inicio = System.currentTimeMillis();
        System.out.println("   Iniciando sincronização...");

        // 1. Obter colunas da tabela (como no Zeus)
        List<String> colunasFirebird = getColunasTabela(firebirdConn, tabelaFirebird);
        if (colunasFirebird.isEmpty()) {
            System.out.println("   Nenhuma coluna encontrada, pulando...");
            return 0;
        }

        // Converter para minúsculas para PostgreSQL
        List<String> colunasPostgres = new ArrayList<>();
        for (String coluna : colunasFirebird) {
            colunasPostgres.add(coluna.toLowerCase());
        }

        // 2. Desabilitar constraints se for tabela pai (como no Zeus)
        if (isTabelaPai(tabelaFirebird)) {
            System.out.println("   Desabilitando constraints...");
            desabilitarConstraints(tabelaPostgres, false);
        }

        // 3. Limpar tabela no PostgreSQL (TRUNCATE mais rápido que DELETE)
        long inicioLimpeza = System.currentTimeMillis();
        int deleted = 0;
        try {
            // Usar TRUNCATE para melhor performance
            postgresJdbcTemplate.execute("TRUNCATE TABLE " + tabelaPostgres + " CASCADE");
            deleted = 1; // TRUNCATE não retorna número de registros
        } catch (Exception e) {
            System.err.println("   Aviso: Erro ao limpar com TRUNCATE, usando DELETE...");
            deleted = postgresJdbcTemplate.update("DELETE FROM " + tabelaPostgres);
        }
        long fimLimpeza = System.currentTimeMillis();
        System.out.println("   Limpeza: " + (deleted == 1 ? "TRUNCATE" : deleted + " registros") +
                " em " + (fimLimpeza - inicioLimpeza) + "ms");

        // 4. Buscar dados do Firebird com tratamento de charset
        long inicioBusca = System.currentTimeMillis();
        List<Map<String, Object>> dadosFirebird = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + tabelaFirebird;

        int totalRegistros = 0;
        try (PreparedStatement pstmt = firebirdConn.prepareStatement(selectQuery);
             ResultSet rs = pstmt.executeQuery()) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                totalRegistros++;
                Map<String, Object> row = new LinkedHashMap<>();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i).toLowerCase();
                    Object value = rs.getObject(i);

                    // Tratamento especial para problemas de charset
                    if (value instanceof String) {
                        // Tentar tratar problemas de encoding
                        String strValue = (String) value;
                        try {
                            // Converter para UTF-8 se necessário
                            value = new String(strValue.getBytes("ISO-8859-1"), "UTF-8");
                        } catch (Exception e) {
                            // Manter valor original se falhar conversão
                            value = strValue;
                        }
                    }

                    row.put(columnName, value);
                }
                dadosFirebird.add(row);
            }
        }

        long fimBusca = System.currentTimeMillis();
        System.out.println("   Busca: " + totalRegistros + " registros em " + (fimBusca - inicioBusca) + "ms");

        if (dadosFirebird.isEmpty()) {
            System.out.println("   Nenhum dado para sincronizar");
            return 0;
        }

        // 5. Inserir dados no PostgreSQL com batch
        long inicioInsercao = System.currentTimeMillis();
        int registrosInseridos = inserirNoPostgres(tabelaPostgres, colunasPostgres, dadosFirebird);
        long fimInsercao = System.currentTimeMillis();

        // 6. Re-habilitar constraints se necessário
        if (isTabelaPai(tabelaFirebird)) {
            System.out.println("   Re-habilitando constraints...");
            desabilitarConstraints(tabelaPostgres, true);
        }

        long fimTotal = System.currentTimeMillis();
        System.out.println("   Inserção: " + registrosInseridos + "/" + totalRegistros +
                " registros em " + (fimInsercao - inicioInsercao) + "ms");
        System.out.println("   Total tabela: " + (fimTotal - inicio) + "ms");

        return registrosInseridos;
    }

    private boolean isTabelaPai(String tabela) {
        List<String> tabelasPai = Arrays.asList(
                "IDENTIFICACAO",
                "ANALISE",
                "AMOSTRA_QUIMICO"
        );
        return tabelasPai.contains(tabela.toUpperCase());
    }

    private void desabilitarConstraints(String tabela, boolean habilitar) {
        try {
            if (habilitar) {
                postgresJdbcTemplate.execute("ALTER TABLE " + tabela + " ENABLE TRIGGER ALL");
            } else {
                postgresJdbcTemplate.execute("ALTER TABLE " + tabela + " DISABLE TRIGGER ALL");
            }
        } catch (Exception e) {
            System.err.println("   Aviso: Não foi possível " + (habilitar ? "habilitar" : "desabilitar") +
                    " constraints da tabela " + tabela + ": " + e.getMessage());
        }
    }

    private List<String> getColunasTabela(Connection conn, String tabela) throws SQLException {
        List<String> colunas = new ArrayList<>();

        String query = "SELECT TRIM(RDB$FIELD_NAME) as field_name " +
                "FROM RDB$RELATION_FIELDS " +
                "WHERE RDB$RELATION_NAME = ? " +
                "ORDER BY RDB$FIELD_POSITION";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, tabela.toUpperCase());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    colunas.add(rs.getString("field_name"));
                }
            }
        }

        return colunas;
    }

    private int inserirNoPostgres(String tabela, List<String> colunas, List<Map<String, Object>> dados) {
        if (dados.isEmpty() || colunas.isEmpty()) {
            return 0;
        }

        // Construir query de INSERT
        String placeholders = String.join(", ", Collections.nCopies(colunas.size(), "?"));
        String insertQuery = "INSERT INTO " + tabela + " (" +
                String.join(", ", colunas) + ") VALUES (" + placeholders + ")";

        // Usar batch para performance (como no Zeus)
        int batchSize = 500;
        int totalInseridos = 0;
        int batchCount = 0;
        long inicioBatchTotal = System.currentTimeMillis();

        List<Object[]> batchArgs = new ArrayList<>();

        for (Map<String, Object> row : dados) {
            Object[] args = new Object[colunas.size()];

            for (int i = 0; i < colunas.size(); i++) {
                String coluna = colunas.get(i);
                Object valor = row.get(coluna);

                // Tratar valores especiais
                if (valor != null) {
                    // Converter BLOB para byte[]
                    if (valor instanceof Blob) {
                        try {
                            Blob blob = (Blob) valor;
                            valor = blob.getBytes(1, (int) blob.length());
                        } catch (Exception e) {
                            System.err.println("   Erro ao converter BLOB: " + e.getMessage());
                            valor = null;
                        }
                    }
                    // Tratar caracteres especiais
                    else if (valor instanceof String) {
                        String strVal = (String) valor;
                        // Remover caracteres de controle
                        strVal = strVal.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                        valor = strVal;
                    }
                }

                args[i] = valor;
            }

            batchArgs.add(args);

            // Executar batch quando atingir o tamanho
            if (batchArgs.size() >= batchSize) {
                batchCount++;
                try {
                    int[] resultados = postgresJdbcTemplate.batchUpdate(insertQuery, batchArgs);
                    totalInseridos += batchArgs.size();
                    batchArgs.clear();
                } catch (Exception e) {
                    System.err.println("      Erro no batch " + batchCount + ": " + e.getMessage());
                    // Tentar inserir um por um para identificar o problema
                    for (int j = 0; j < batchArgs.size(); j++) {
                        try {
                            postgresJdbcTemplate.update(insertQuery, batchArgs.get(j));
                            totalInseridos++;
                        } catch (Exception ex) {
                            System.err.println("      Erro no registro " + (totalInseridos + 1) + ": " + ex.getMessage());
                        }
                    }
                    batchArgs.clear();
                }
            }
        }

        // Executar restante
        if (!batchArgs.isEmpty()) {
            batchCount++;
            try {
                int[] resultados = postgresJdbcTemplate.batchUpdate(insertQuery, batchArgs);
                totalInseridos += batchArgs.size();
            } catch (Exception e) {
                System.err.println("      Erro no batch final " + batchCount + ": " + e.getMessage());
                // Inserir um por um
                for (Object[] args : batchArgs) {
                    try {
                        postgresJdbcTemplate.update(insertQuery, args);
                        totalInseridos++;
                    } catch (Exception ex) {
                        System.err.println("      Erro no registro final: " + ex.getMessage());
                    }
                }
            }
        }

        long fimBatchTotal = System.currentTimeMillis();
        System.out.println("      Total batches: " + batchCount + " em " + (fimBatchTotal - inicioBatchTotal) + "ms");

        return totalInseridos;
    }
}