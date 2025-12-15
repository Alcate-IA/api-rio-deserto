package com.rioDesertoAcessoDb.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    private static final String FIREBIRD_URL = "jdbc:firebirdsql://192.168.108.88:3050//dataib/rdlab.fdb?encoding=UTF8";
    private static final String FIREBIRD_USER = "ALCATEIA";
    private static final String FIREBIRD_PASSWORD = "8D5Z9s2F";

     @Scheduled(cron = "0 0 4 * * MON-FRI", zone = "America/Sao_Paulo")
//     @Scheduled(fixedDelay = 500000000, initialDelay = 6000)
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

         inserirParametrosLegislacaoAposSync();

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

        // Verificar se é a tabela ANALISE para tratamento especial
        boolean isTabelaAnalise = "ANALISE".equalsIgnoreCase(tabelaFirebird);

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

        // Se for tabela ANALISE, adicionar coluna id_analise
        if (isTabelaAnalise) {
            System.out.println("   [ANALISE] Adicionando coluna id_analise para geração automática");
            colunasPostgres.add(0, "id_analise"); // Adiciona no início da lista
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
        int idAnaliseCounter = 1; // Contador para id_analise
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
                    Object[] args;

                    if (isTabelaAnalise) {
                        // Para tabela ANALISE, criar array com espaço para id_analise
                        args = new Object[columnCount + 1];
                        args[0] = idAnaliseCounter++; // Preencher id_analise sequencial

                        // Copiar demais valores do Firebird
                        for (int i = 0; i < columnCount; i++) {
                            Object value = rs.getObject(i + 1);
                            args[i + 1] = tratarValor(value);
                        }
                    } else {
                        // Para outras tabelas, processar normalmente
                        args = new Object[columnCount];
                        for (int i = 0; i < columnCount; i++) {
                            Object value = rs.getObject(i + 1);
                            args[i] = tratarValor(value);
                        }
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
//            try {
//                // Tentar corrigir charset ISO-8859-1 -> UTF-8
//                str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
//            } catch (Exception e) {
//                // Ignora se falhar
//            }
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

    @Transactional
    public void inserirParametrosLegislacaoAposSync() {
        System.out.println("\n=== INICIANDO INSERÇÃO DE PARÂMETROS DE LEGISLAÇÃO ===");
        System.out.println("Data/Hora: " + new Date());

        long inicio = System.currentTimeMillis();
        int totalInseridos = 0;
        int totalErros = 0;

        // Lista de parâmetros para inserir: {id_analise, id_legislacao, parametro}
        Object[][] parametros = {
                {68, 1, "entre 5 e 9"},
                {117, 1, "inferior a 40"},
                {142, 1, "até 1"},
                {139, 1, "remoção mínima de 60%"},
                {473, 1, "máx 0,5"},
                {474, 1, "máx 5,0"},
                {516, 1, "máx 5,0"},
                {171, 1, "máx 0,2"},
                {156, 1, "máx 0,5"},
                {519, 1, "máx 0,2"},
                {224, 1, "máx 1,0"},
                {114, 1, "máx 0,1"},
                {520, 1, "máx 4,0"},
                {197, 1, "máx 15,0"},
                {184, 1, "máx 10,0"},
                {223, 1, "máx 1,0"},
                {476, 1, "máx 0,01"},
                {546, 1, "máx 2,0"},
                {316, 1, "máx 20,0"},
                {170, 1, "máx 0,1"},
                {472, 1, "máx 0,30"},
                {596, 1, "máx 1,0"},
                {165, 1, "máx 5,0"},
                {137, 1, "máx 0,5"},
                {151, 2, "máx 200"},
                {473, 2, "máx 10"},
                {474, 2, "máx 700"},
                {516, 2, "máx 500"},
                {171, 2, "máx 5"},
                {156, 2, "máx 10"},
                {519, 2, "máx 70"},
                {83, 2, "máx 250000"},
                {154, 2, "máx 2000"},
                {475, 2, "máx 50"},
                {162, 2, "máx 300"},
                {184, 2, "máx 1500"},
                {163, 2, "máx 100"},
                {476, 2, "máx 1"},
                {555, 2, "máx 70"},
                {546, 2, "máx 20"},
                {185, 2, "máx 10000"},
                {149, 2, "máx 1000"},
                {170, 2, "máx 100"},
                {472, 2, "máx 10"},
                {166, 2, "máx 200000"},
                {141, 2, "máx 1000000"},
                {110, 2, "máx 250000"},
                {621, 2, "máx 50"},
                {165, 2, "máx 5000"},
                {137, 2, "máx 3"},
                {109, 2, "Ausentes em 100 mL"},
                {121, 2, "Ausentes em 100 mL"},
                {151, 3, "máx 3500"},
                {473, 3, "máx 10"},
                {474, 3, "máx 700"},
                {516, 3, "máx 500"},
                {171, 3, "máx 5"},
                {156, 3, "máx 10"},
                {154, 3, "máx 2000"},
                {475, 3, "máx 50"},
                {162, 3, "máx 2450"},
                {163, 3, "máx 400"},
                {476, 3, "máx 1"},
                {555, 3, "máx 70"},
                {546, 3, "máx 20"},
                {185, 3, "máx 10000"},
                {170, 3, "máx 50"},
                {472, 3, "máx 10"},
                {165, 3, "máx 1050"},
                {137, 3, "máx 140"},
                {68, 4, "6,0 a 9,0"},
                {131, 4, "mín 5,0"},
                {139, 4, "máx 5,0"},
                {144, 4, "máx 100"},
                {204, 4, "máx 75"},
                {121, 4, "máx 1.000 (em 80% das amostras)"},
                {226, 4, "máx 0,1"},
                {473, 4, "máx 0,01"},
                {474, 4, "máx 0,7"},
                {516, 4, "máx 0,5"},
                {171, 4, "máx 0,001"},
                {156, 4, "máx 0,01"},
                {519, 4, "máx 0,005"},
                {83, 4, "máx 250"},
                {82, 4, "máx 0,01"},
                {224, 4, "máx 0,009"},
                {475, 4, "máx 0,05"},
                {197, 4, "máx 0,3"},
                {184, 4, "máx 1,4"},
                {127, 4, "máx 0,030"},
                {163, 4, "máx 0,1"},
                {476, 4, "máx 0,0002"},
                {546, 4, "máx 0,025"},
                {185, 4, "máx 10,0"},
                {149, 4, "máx 1,0"},
                {316, 4, "máx 3,7 (pH ≤ 7,5)"},
                {170, 4, "máx 0,01"},
                {472, 4, "máx 0,01"},
                {141, 4, "máx 500"},
                {110, 4, "máx 250"},
                {143, 4, "máx 0,002"},
                {165, 4, "máx 0,18"},
                {137, 4, "máx 0,003"},
                {68, 5, "Entre 6,0 e 9,0"},
                {146, 5, "30,0 mg/L"},
                {114, 5, "0,1 mg/L"},
                {154, 5, "0,5 mg/L"},
                {171, 5, "0,1 mg/L"},
                {476, 5, "0,005 mg/L"},
                {546, 5, "1,0 mg/L"},
                {165, 5, "1,0 mg/L"},
                {473, 5, "0,1 mg/L"},
                {170, 5, "0,02 mg/L"},
                {472, 5, "0,02 mg/L"},
                {223, 5, "1,0 mg/L"},
                {137, 5, "0,2 mg/L"}
        };

        // Primeiro, limpar a tabela existente (opcional - comente se não quiser limpar)
        try {
            System.out.println("Limpando tabela parametros_legislacao...");
            postgresJdbcTemplate.execute("TRUNCATE TABLE parametros_legislacao RESTART IDENTITY CASCADE");
            System.out.println("Tabela limpa com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao limpar tabela: " + e.getMessage());
            // Continua tentando inserir mesmo se falhar ao limpar
        }

        // Inserir cada parâmetro
        String insertSQL = "INSERT INTO parametros_legislacao (id_analise, id_legislacao, parametro) VALUES (?, ?, ?)";

        for (Object[] param : parametros) {
            try {
                int idAnalise = ((Integer) param[0]).intValue();
                int idLegislacao = ((Integer) param[1]).intValue();
                String parametro = (String) param[2];

                // Verificar se a análise existe
                String checkSQL = "SELECT COUNT(*) FROM analise WHERE id_analise = ?";
                Integer count = postgresJdbcTemplate.queryForObject(checkSQL, Integer.class, idAnalise);

                if (count != null && count > 0) {
                    postgresJdbcTemplate.update(insertSQL, idAnalise, idLegislacao, parametro);
                    totalInseridos++;

                    // Feedback a cada 10 inserções
                    if (totalInseridos % 10 == 0) {
                        System.out.println("  Inseridos " + totalInseridos + " registros...");
                    }
                } else {
                    System.err.println("  AVISO: Análise com id " + idAnalise + " não encontrada na tabela analise.");
                    totalErros++;
                }

            } catch (Exception e) {
                totalErros++;
                System.err.println("  ERRO ao inserir parâmetro: " + Arrays.toString(param) + " - " + e.getMessage());
            }
        }

        long fim = System.currentTimeMillis();
        System.out.println("\n=== INSERÇÃO DE PARÂMETROS CONCLUÍDA ===");
        System.out.println("Total inseridos: " + totalInseridos);
        System.out.println("Total erros: " + totalErros);
        System.out.println("Tempo total: " + (fim - inicio) + "ms");
        System.out.println("========================================\n");
    }
}