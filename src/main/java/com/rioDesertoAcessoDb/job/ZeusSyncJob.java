package com.rioDesertoAcessoDb.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

@Component
public class ZeusSyncJob {

    @Autowired
    private JdbcTemplate postgresJdbcTemplate;

    private static final List<String> TABELAS_ORDENADAS = Arrays.asList(
            "TB_EMPRESA",
            "TB_PESSOA",
            "TB_PIEZOMETRO",
            "TB_CALIBRACAO_EQUIPAMENTO",

            "TB_METEOROLOGIA",
            "TB_NIVEL_AGUA",
            "TB_RECURSOS_HIDRICOS",
            "TB_INSPECAO_PIEZOMETRO",

            "TB_METEOROLOGIA_ITEM",
            "TB_NIVEL_AGUA_ITEM",
            "TB_RECURSOS_HIDRICOS_ITEM",
            "TB_INSPECAO_PIEZOMETRO_FREQ",
            "TB_INSPECAO_PIEZOMETRO_MVTO");

    // @Scheduled(fixedDelay = 60000, initialDelay = 5000)
    @Scheduled(cron = "0 0 6 * * MON-FRI", zone = "America/Sao_Paulo")
    public void testConnections() {
        System.out.println("=== SINCRONIZACAO DE DADOS ZEUS ===");

        long inicioTeste = System.currentTimeMillis();

        // 1. Testar PostgreSQL
        System.out.println("1. Testando PostgreSQL...");
        long inicioPostgres = System.currentTimeMillis();
        try {
            String pgVersion = postgresJdbcTemplate.queryForObject("SELECT version()", String.class);
            long fimPostgres = System.currentTimeMillis();
            System.out.println("   PostgreSQL OK: " + pgVersion.split(",")[0]);
            System.out.println("   Tempo conexao PostgreSQL: " + (fimPostgres - inicioPostgres) + "ms");
        } catch (Exception e) {
            System.err.println("   PostgreSQL FALHOU: " + e.getMessage());
            return;
        }

        // 2. Testar Firebird
        System.out.println("2. Testando Firebird...");
        long inicioFirebird = System.currentTimeMillis();
        try (Connection fbConn = DriverManager.getConnection(
                "jdbc:firebirdsql://192.9.200.7:3050//data1/dataib/zeus20.fdb?encoding=WIN1252",
                "ALCATEIA",
                "8D5Z9s2F")) {

            long fimFirebird = System.currentTimeMillis();
            String fbProduct = fbConn.getMetaData().getDatabaseProductName();
            String fbVersion = fbConn.getMetaData().getDatabaseProductVersion();
            System.out.println("   Firebird OK: " + fbProduct + " - " + fbVersion);
            System.out.println("   Tempo conexao Firebird: " + (fimFirebird - inicioFirebird) + "ms");

            // 3. Sincronizar tabelas na ordem correta
            System.out.println("3. Iniciando sincronizacao de tabelas...");
            long inicioSincronizacaoTotal = System.currentTimeMillis();
            sincronizarTodasTabelasOrdenadas(fbConn);
            long fimSincronizacaoTotal = System.currentTimeMillis();
            System.out.println(
                    "   Tempo total sincronizacao: " + (fimSincronizacaoTotal - inicioSincronizacaoTotal) + "ms");

        } catch (Exception e) {
            System.err.println("   Firebird FALHOU: " + e.getMessage());
            e.printStackTrace();
        }

        // 4. Atribuir classificações aleatórias aos piezômetros ativos
        atribuirClassificacoesAleatorias();

        long fimTeste = System.currentTimeMillis();
        System.out.println("Tempo total: " + (fimTeste - inicioTeste) + "ms");
        System.out.println("=== FIM DA SINCRONIZACAO ===\n");
    }

    private void sincronizarTodasTabelasOrdenadas(Connection firebirdConn) {
        long inicioTotal = System.currentTimeMillis();
        int tabelasProcessadas = 0;
        int tabelasComErro = 0;

        for (String tabela : TABELAS_ORDENADAS) {
            // Verificar se a tabela existe no Firebird
            if (!tabelaExisteNoFirebird(firebirdConn, tabela)) {
                System.out.println("Tabela " + tabela + " nao encontrada no Firebird, pulando...");
                continue;
            }

            try {
                sincronizarTabelaCompleta(firebirdConn, tabela);
                tabelasProcessadas++;
            } catch (Exception e) {
                tabelasComErro++;
                System.err.println("Erro ao sincronizar tabela " + tabela + ": " + e.getMessage());
                // Continua com a próxima tabela
            }
        }

        long fimTotal = System.currentTimeMillis();
        System.out.println("\nRESUMO FINAL:");
        System.out.println("   Tabelas processadas: " + tabelasProcessadas);
        System.out.println("   Tabelas com erro: " + tabelasComErro);
        System.out.println("   Tempo total: " + (fimTotal - inicioTotal) + "ms");
    }

    private boolean tabelaExisteNoFirebird(Connection conn, String tabela) {
        try {
            String query = "SELECT 1 FROM RDB$RELATIONS WHERE RDB$RELATION_NAME = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, tabela.toUpperCase());
                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

    private void sincronizarTabelaCompleta(Connection firebirdConn, String tabelaFirebird) throws SQLException {
        String tabelaPostgres = tabelaFirebird.toLowerCase();
        System.out.println("\nSincronizando " + tabelaFirebird);
        long inicioTabela = System.currentTimeMillis();

        try {
            // 1. Obter colunas da tabela
            long inicioEstrutura = System.currentTimeMillis();
            List<String> colunasFirebird = getColunasTabela(firebirdConn, tabelaFirebird);
            List<String> colunasPostgres = new ArrayList<>();
            for (String coluna : colunasFirebird) {
                colunasPostgres.add(coluna.toLowerCase());
            }
            long fimEstrutura = System.currentTimeMillis();

            // 2. Desabilitar FKs temporariamente se for tabela pai
            if (isTabelaPai(tabelaFirebird)) {
                System.out.println("   Desabilitando constraints para tabela pai...");
                desabilitarConstraints(tabelaPostgres, false);
            }

            // 3. Limpar tabela no PostgreSQL
            long inicioLimpeza = System.currentTimeMillis();
            int deleted = postgresJdbcTemplate.update("DELETE FROM " + tabelaPostgres);
            long fimLimpeza = System.currentTimeMillis();

            // 4. Buscar dados do Firebird
            long inicioBusca = System.currentTimeMillis();
            List<Map<String, Object>> dadosFirebird = new ArrayList<>();
            String selectQuery = "SELECT * FROM " + tabelaFirebird;

            try (Statement stmt = firebirdConn.createStatement();
                    ResultSet rs = stmt.executeQuery(selectQuery)) {

                int totalRegistros = 0;
                while (rs.next()) {
                    totalRegistros++;
                    Map<String, Object> row = new LinkedHashMap<>();
                    ResultSetMetaData metaData = rs.getMetaData();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        String columnName = metaData.getColumnName(i);
                        Object value = rs.getObject(i);
                        row.put(columnName.toLowerCase(), value);
                    }
                    dadosFirebird.add(row);
                }

                long fimBusca = System.currentTimeMillis();
                System.out.println("   Registros encontrados: " + totalRegistros);
                System.out.println("   Tempo busca: " + (fimBusca - inicioBusca) + "ms");

                if (dadosFirebird.isEmpty()) {
                    System.out.println("   Nenhum dado para sincronizar");
                    return;
                }

                // 5. Inserir dados no PostgreSQL
                long inicioInsercao = System.currentTimeMillis();
                int registrosInseridos = inserirNoPostgres(tabelaPostgres, colunasPostgres, dadosFirebird);
                long fimInsercao = System.currentTimeMillis();

                // 7. Re-habilitar FKs se necessário
                if (isTabelaPai(tabelaFirebird)) {
                    System.out.println("   Re-habilitando constraints...");
                    desabilitarConstraints(tabelaPostgres, true);
                }

                long fimTabela = System.currentTimeMillis();

                // Resumo da tabela
                System.out.println("   Resumo " + tabelaFirebird + ":");
                System.out.println("      - Estrutura: " + (fimEstrutura - inicioEstrutura) + "ms");
                System.out.println("      - Limpeza: " + deleted + " registros deletados em "
                        + (fimLimpeza - inicioLimpeza) + "ms");
                System.out.println("      - Busca Firebird: " + totalRegistros + " registros em "
                        + (fimBusca - inicioBusca) + "ms");
                System.out.println("      - Insercao PostgreSQL: " + registrosInseridos + " registros em "
                        + (fimInsercao - inicioInsercao) + "ms");
                System.out.println("      - Tempo total: " + (fimTabela - inicioTabela) + "ms");
            }

        } catch (Exception e) {
            System.err.println("   ERRO na sincronizacao da tabela " + tabelaFirebird + ": " + e.getMessage());
            // Tenta re-habilitar constraints mesmo com erro
            try {
                if (isTabelaPai(tabelaFirebird)) {
                    desabilitarConstraints(tabelaFirebird.toLowerCase(), true);
                }
            } catch (Exception ex) {
                // Ignora erro ao re-habilitar
            }
            throw e;
        }
    }

    private boolean isTabelaPai(String tabela) {
        List<String> tabelasPai = Arrays.asList(
                "TB_EMPRESA",
                "TB_PIEZOMETRO",
                "TB_CALIBRACAO_EQUIPAMENTO",
                "TB_METEOROLOGIA",
                "TB_NIVEL_AGUA",
                "TB_RECURSOS_HIDRICOS",
                "TB_INSPECAO_PIEZOMETRO");
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
            System.err.println("   Aviso: Nao foi possivel " + (habilitar ? "habilitar" : "desabilitar") +
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
        String placeholders = String.join(", ", Collections.nCopies(colunas.size(), "?"));
        String insertQuery = "INSERT INTO " + tabela + " (" + String.join(", ", colunas) + ") VALUES (" + placeholders
                + ")";

        // Executar batch insert
        int batchSize = 500;
        int totalProcessados = 0;
        long inicioBatchTotal = System.currentTimeMillis();
        int batchCount = 0;

        for (int i = 0; i < dados.size(); i += batchSize) {
            batchCount++;
            int fim = Math.min(i + batchSize, dados.size());
            List<Map<String, Object>> batch = dados.subList(i, fim);

            // Preparar batch
            List<Object[]> batchArgs = new ArrayList<>();
            for (Map<String, Object> row : batch) {
                Object[] args = new Object[colunas.size()];
                for (int j = 0; j < colunas.size(); j++) {
                    args[j] = row.get(colunas.get(j));
                }
                batchArgs.add(args);
            }

            // Executar batch
            try {
                long inicioBatch = System.currentTimeMillis();
                int[] results = postgresJdbcTemplate.batchUpdate(insertQuery, batchArgs);
                long fimBatch = System.currentTimeMillis();

                totalProcessados += batch.size();
            } catch (Exception e) {
                System.err.println("      Erro no batch " + batchCount + ": " + e.getMessage());
                throw e;
            }
        }

        long fimBatchTotal = System.currentTimeMillis();
        System.out.println("      Total batches: " + batchCount + " em " + (fimBatchTotal - inicioBatchTotal) + "ms");

        return totalProcessados;
    }

    // Isso aqui vai sair, é só para o mommento
    private void atribuirClassificacoesAleatorias() {
        System.out.println("\n4. Atribuindo classificações aleatórias aos piezômetros ativos...");
        long inicioClassificacao = System.currentTimeMillis();

        try {
            // IDs possíveis de classificação (1 a 4)
            int[] classificacoesDisponiveis = { 1, 2, 3, 4 };
            Random random = new Random();

            // Buscar piezômetros ativos da empresa 18
            String selectQuery = "SELECT cd_piezometro FROM tb_piezometro WHERE fg_situacao = 'A' AND cd_empresa = '18'";
            List<Integer> piezometrosAtivos = postgresJdbcTemplate.queryForList(selectQuery, Integer.class);

            if (piezometrosAtivos.isEmpty()) {
                System.out.println("   Nenhum piezômetro ativo encontrado para a empresa 18");
                return;
            }

            System.out.println("   Piezômetros ativos encontrados: " + piezometrosAtivos.size());

            // Atualizar cada piezômetro com uma classificação aleatória
            String updateQuery = "UPDATE tb_piezometro SET id_classificacao_agua = ? WHERE cd_piezometro = ?";
            int piezometrosAtualizados = 0;

            for (Integer cdPiezometro : piezometrosAtivos) {
                // Selecionar uma classificação aleatória
                int classificacaoAleatoria = classificacoesDisponiveis[random
                        .nextInt(classificacoesDisponiveis.length)];

                // Atualizar o piezômetro
                int rowsAffected = postgresJdbcTemplate.update(updateQuery, classificacaoAleatoria, cdPiezometro);

                if (rowsAffected > 0) {
                    piezometrosAtualizados++;
                }
            }

            long fimClassificacao = System.currentTimeMillis();
            System.out.println("   Piezômetros atualizados com classificação: " + piezometrosAtualizados);
            System.out.println(
                    "   Tempo atribuição de classificações: " + (fimClassificacao - inicioClassificacao) + "ms");

        } catch (Exception e) {
            System.err.println("   ERRO ao atribuir classificações aleatórias: " + e.getMessage());
            e.printStackTrace();
        }
    }
}