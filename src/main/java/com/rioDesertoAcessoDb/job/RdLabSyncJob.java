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
            "AMOSTRAANALISE_QUIMICO");

    private static final String FIREBIRD_URL = "jdbc:firebirdsql://192.168.108.88:3050//dataib/rdlab.fdb";
    private static final String FIREBIRD_USER = "ALCATEIA";
    private static final String FIREBIRD_PASSWORD = "8D5Z9s2F";

    @Scheduled(cron = "0 0 4 * * MON-FRI", zone = "America/Sao_Paulo")
    // @Scheduled(fixedDelay = 60000, initialDelay = 5000)
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
            System.out
                    .println("Tempo total sincronização: " + (fimSincronizacaoTotal - inicioSincronizacaoTotal) + "ms");

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

    private int sincronizarDadosTabela(Connection firebirdConn, String tabelaFirebird, String tabelaPostgres)
            throws SQLException {
        long inicio = System.currentTimeMillis();
        System.out.println("   Iniciando sincronização (modo streaming)...");

        // Constantes de otimização
        final int FETCH_SIZE = 2000; // Quantos registros buscar por vez do Firebird
        final int BATCH_SIZE = 1000; // Quantos registros inserir por batch no PostgreSQL

        // 1. Obter colunas da tabela (com otimização para tabelas grandes)
        List<String> colunasFirebird = getColunasParaSync(firebirdConn, tabelaFirebird);
        if (colunasFirebird.isEmpty()) {
            System.out.println("   Nenhuma coluna encontrada, pulando...");
            return 0;
        }

        // Converter para minúsculas para PostgreSQL
        List<String> colunasPostgres = new ArrayList<>();
        for (String coluna : colunasFirebird) {
            colunasPostgres.add(coluna.toLowerCase());
        }

        // 2. Desabilitar constraints se for tabela pai
        if (isTabelaPai(tabelaFirebird)) {
            System.out.println("   Desabilitando constraints...");
            desabilitarConstraints(tabelaPostgres, false);
        }

        // 3. Limpar tabela no PostgreSQL (TRUNCATE mais rápido que DELETE)
        long inicioLimpeza = System.currentTimeMillis();
        try {
            postgresJdbcTemplate.execute("TRUNCATE TABLE " + tabelaPostgres + " CASCADE");
            System.out.println("   Limpeza: TRUNCATE em " + (System.currentTimeMillis() - inicioLimpeza) + "ms");
        } catch (Exception e) {
            System.err.println("   Aviso: Erro ao limpar com TRUNCATE, usando DELETE...");
            postgresJdbcTemplate.update("DELETE FROM " + tabelaPostgres);
            System.out.println("   Limpeza: DELETE em " + (System.currentTimeMillis() - inicioLimpeza) + "ms");
        }

        // 4. Preparar query de INSERT
        String placeholders = String.join(", ", Collections.nCopies(colunasPostgres.size(), "?"));
        String insertQuery = "INSERT INTO " + tabelaPostgres + " (" +
                String.join(", ", colunasPostgres) + ") VALUES (" + placeholders + ")";

        // 5. STREAMING: Buscar e inserir simultaneamente
        long inicioMigracao = System.currentTimeMillis();
        int totalRegistros = 0;
        int totalInseridos = 0;
        int batchCount = 0;
        List<Object[]> batchArgs = new ArrayList<>(BATCH_SIZE);

        String selectQuery = "SELECT " + String.join(", ", colunasFirebird) + " FROM " + tabelaFirebird;

        // Usar TYPE_FORWARD_ONLY e CONCUR_READ_ONLY para streaming eficiente
        try (PreparedStatement pstmt = firebirdConn.prepareStatement(
                selectQuery,
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY)) {

            // IMPORTANTE: Define o fetch size para não carregar tudo na memória
            pstmt.setFetchSize(FETCH_SIZE);

            try (ResultSet rs = pstmt.executeQuery()) {
                int columnCount = colunasFirebird.size();

                while (rs.next()) {
                    totalRegistros++;

                    // Extrair valores da linha atual
                    Object[] args = new Object[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        Object value = rs.getObject(i + 1);
                        args[i] = tratarValor(value);
                    }

                    batchArgs.add(args);

                    // Quando atingir BATCH_SIZE, inserir e limpar memória
                    if (batchArgs.size() >= BATCH_SIZE) {
                        long inicioBatch = System.currentTimeMillis();
                        totalInseridos += executarBatch(insertQuery, batchArgs);
                        long tempoBatch = System.currentTimeMillis() - inicioBatch;
                        batchArgs.clear();
                        batchCount++;

                        // Feedback visual a cada 10 batches (10.000 registros)
                        if (batchCount % 10 == 0) {
                            long tempoTotal = System.currentTimeMillis() - inicioMigracao;
                            System.out.println(
                                    "   -> " + (batchCount * BATCH_SIZE / 1000) + "k processados... (último batch: "
                                            + tempoBatch + "ms, total: " + (tempoTotal / 1000) + "s)");
                        }
                    }
                }

                // Inserir registros restantes
                if (!batchArgs.isEmpty()) {
                    totalInseridos += executarBatch(insertQuery, batchArgs);
                    batchCount++;
                }
            }
        }

        long fimMigracao = System.currentTimeMillis();
        System.out.println("   Migração: " + totalInseridos + "/" + totalRegistros +
                " registros em " + (fimMigracao - inicioMigracao) + "ms (" + batchCount + " batches)");

        // 6. Re-habilitar constraints se necessário
        if (isTabelaPai(tabelaFirebird)) {
            System.out.println("   Re-habilitando constraints...");
            desabilitarConstraints(tabelaPostgres, true);
        }

        long fimTotal = System.currentTimeMillis();
        System.out.println("   Total tabela: " + (fimTotal - inicio) + "ms");

        return totalInseridos;
    }

    private Object tratarValor(Object value) {
        if (value == null)
            return null;

        // IGNORAR BLOBs - são muito pesados e não são necessários
        if (value instanceof Blob) {
            return null;
        } else if (value instanceof String) {
            String str = (String) value;
            try {
                // Tentar corrigir charset ISO-8859-1 -> UTF-8
                str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
            } catch (Exception e) {
                // Ignora se falhar
            }
            // Remover caracteres de controle
            return str.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
        }
        return value;
    }

    private int executarBatch(String sql, List<Object[]> batch) {
        long inicio = System.currentTimeMillis();
        try {
            postgresJdbcTemplate.batchUpdate(sql, batch);
            return batch.size();
        } catch (Exception e) {
            System.err.println("\n   ⚠ Erro no batch (" + batch.size() + " registros): " + e.getMessage());
            System.err.println("   Tentando inserir um a um (isso será lento)...");
            int inseridos = 0;
            int erros = 0;
            for (int i = 0; i < batch.size(); i++) {
                try {
                    postgresJdbcTemplate.update(sql, batch.get(i));
                    inseridos++;
                } catch (Exception ex) {
                    erros++;
                    if (erros <= 3) { // Só mostra os 3 primeiros erros
                        System.err.println("   Erro registro " + i + ": " + ex.getMessage());
                    }
                }
            }
            long tempo = System.currentTimeMillis() - inicio;
            System.err.println("   Fallback concluído: " + inseridos + " ok, " + erros + " erros, " + tempo + "ms");
            return inseridos;
        }
    }

    private boolean isTabelaPai(String tabela) {
        List<String> tabelasPai = Arrays.asList(
                "IDENTIFICACAO",
                "ANALISE",
                "AMOSTRA_QUIMICO");
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

    /**
     * ========================================================================
     * CONFIGURAÇÃO DE COLUNAS PARA SINCRONIZAÇÃO
     * ========================================================================
     * Este método define quais colunas serão sincronizadas de cada tabela.
     * Para tabelas grandes, sincronizamos apenas colunas essenciais para
     * performance.
     * 
     * IMPORTANTE: Os nomes das colunas devem estar em MAIÚSCULAS (padrão Firebird)
     * ========================================================================
     */
    private List<String> getColunasParaSync(Connection conn, String tabela) throws SQLException {

        // ====================================================================
        // TABELA: AMOSTRAANALISE_QUIMICO (~800k registros)
        // ====================================================================
        // Para adicionar novas colunas, inclua o nome (MAIÚSCULO) na lista abaixo.
        // Exemplo: Arrays.asList("N_REGISTRO", "SIMBOLO", "RESULTADO", "NOVA_COLUNA")
        //
        // Colunas disponíveis na tabela original:
        // N_REGISTRO, SIMBOLO, RESULTADO, VALOR1-9, IMG, IMG2, PROCEDIMENTO,
        // INCERTEZA, EXECUTANTE, CONFERENTE
        // ====================================================================
        if ("AMOSTRAANALISE_QUIMICO".equalsIgnoreCase(tabela)) {
            System.out.println("   [OTIMIZAÇÃO] Buscando apenas colunas essenciais");
            return Arrays.asList(
                    "N_REGISTRO", // Chave primária
                    "SIMBOLO", // Símbolo da análise
                    "RESULTADO" // Resultado da análise
            // ADICIONE NOVAS COLUNAS AQUI (separadas por vírgula)
            );
        }

        // ====================================================================
        // TABELA: AMOSTRA_QUIMICO (~70k registros)
        // ====================================================================
        // Para adicionar novas colunas, inclua o nome (MAIÚSCULO) na lista abaixo.
        // Exemplo: Arrays.asList("N_REGISTRO", "DATA", ..., "NOVA_COLUNA")
        //
        // Colunas disponíveis na tabela original:
        // N_REGISTRO, DATA, IDENTIFICACAO, COMPLEMENTO, DT_COLETA, SETOR,
        // DT_PREVISTA, HORA_COLETA, TEMPERATURA, TEMPO, COLETOR, INTERESSADO,
        // PRAZO, DT_CONCLUSAO, OBSERVACAO, LOCAL, FORNECEDOR, APROVAR, RNC,
        // LOTE, NUMORCA, TIPOAMOSTRA, PONTOCOLETA, DATAANALISES, DATAFABRICA,
        // DATAVALIDADE, CLIENTE, TEMPAMOSTRA, HR_RECEBE, HR_CONCLUSAO, IMG,
        // ADSORCAO, DT_APROVACAO, TECNICO, AVISAR, AREA, ID_AREA, PRIORIDADE,
        // REPROGRAMAR
        // ====================================================================
        if ("AMOSTRA_QUIMICO".equalsIgnoreCase(tabela)) {
            System.out.println("   [OTIMIZAÇÃO] Buscando apenas colunas essenciais");
            return Arrays.asList(
                    "N_REGISTRO", // Chave primária
                    "DATA", // Data da coleta
                    "IDENTIFICACAO", // Código de identificação
                    "COLETOR", // Nome do coletor
                    "TIPOAMOSTRA" // Tipo de amostra
            // ADICIONE NOVAS COLUNAS AQUI (separadas por vírgula)
            );
        }

        // ====================================================================
        // OUTRAS TABELAS (IDENTIFICACAO, ANALISE, etc.)
        // ====================================================================
        // Para tabelas menores, sincronizamos todas as colunas automaticamente.
        // Se quiser otimizar alguma outra tabela, adicione um bloco similar acima.
        // ====================================================================
        return getColunasTabela(conn, tabela);
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
}