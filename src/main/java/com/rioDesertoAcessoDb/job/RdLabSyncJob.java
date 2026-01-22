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

        // Se for tabela ANALISE, NÃO adicionar id_analise manualmente.
        // Vamos deixar o PostgreSQL gerar via IDENTITY/SERIAL.
        if (isTabelaAnalise) {
            System.out.println("   [ANALISE] Usando id_analise auto-gerado pelo PostgreSQL para novos registros.");
        }

        // 2. Desabilitar constraints se for tabela pai
        if (isTabelaPai(tabelaFirebird)) {
            System.out.println("   Desabilitando constraints...");
            desabilitarConstraints(tabelaPostgres, false);
        }

        // 3. Limpar tabela no PostgreSQL (TRUNCATE mais rápido que DELETE)
        // EXCEÇÃO: Tabela ANALISE não deve ser limpa para preservar IDs
        long inicioLimpeza = System.currentTimeMillis();
        if (isTabelaAnalise) {
            System.out.println("   [ANALISE] Pulando limpeza da tabela (modo incremental)");
        } else {
            try {
                postgresJdbcTemplate.execute("TRUNCATE TABLE " + tabelaPostgres + " CASCADE");
                System.out.println("   Limpeza: TRUNCATE em " + (System.currentTimeMillis() - inicioLimpeza) + "ms");
            } catch (Exception e) {
                System.err.println("   Aviso: Erro ao limpar com TRUNCATE, usando DELETE...");
                postgresJdbcTemplate.update("DELETE FROM " + tabelaPostgres);
                System.out.println("   Limpeza: DELETE em " + (System.currentTimeMillis() - inicioLimpeza) + "ms");
            }
        }

        // 4. Preparar query de INSERT
        String placeholders = String.join(", ", Collections.nCopies(colunasPostgres.size(), "?"));
        String insertQuery = "INSERT INTO " + tabelaPostgres + " (" +
                String.join(", ", colunasPostgres) + ") VALUES (" + placeholders + ")";

        if (isTabelaAnalise) {
            insertQuery += " ON CONFLICT (simbolo, laboratorio) DO NOTHING";
        }

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
            // try {
            // // Tentar corrigir charset ISO-8859-1 -> UTF-8
            // str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
            // } catch (Exception e) {
            // // Ignora se falhar
            // }
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
                // Legislação 1: Resolução CONAMA nº 430/2011 (Lançamento de Efluentes)


                //V1: -------------------------------------
//                { 508, 1, "0,5 mg/L" }, // Arsênio Total
//                { 169, 1, "5,0 mg/L" }, // Bário Total
//                { 512, 1, "0,2 mg/L " }, // Cádmio Total
//                { 513, 1, "0,5 mg/L" }, // Chumbo Total
//                { 220, 1, "1,0 mg/L" }, // Cobre Dissolvido
//                { 111, 1, "0,1 mg/L" }, // Cromo Hexavalente
//                { 194, 1, "15,0 mg/L" }, // Ferro Dissolvido
//                { 181, 1, "10,0 mg/L" }, // Fluoreto Total
//                { 219, 1, "1,0 mg/L" }, // Manganês Dissolvido
//                { 510, 1, "0,01 mg/L" }, // Mercúrio Total
//                { 541, 1, "2,0 mg/L" }, // Níquel Total [cite: 626]
//                { 167, 1, "0,1 mg/L" }, // Prata Total [cite: 626]
//                { 657, 1, "0,30 mg/L" }, // Selênio Total [cite: 626]
//                { 162, 1, "5,0 mg/L" }, // Zinco Total [cite: 626]
//                { 102, 1, "pH entre 5 a 9" }, // pH [cite: 613]
//                { 136, 1, "DBO 5 dias: remoção mínima de 60%" }, // DBO [cite: 623]
//                { 139, 1, "Materiais sedimentáveis: até 1 mL/L" }, // Resíduos Sedimentáveis [cite: 615]
                // -------------------------------------



                { 114, 1, "40°C max" }, // Temperatura 40 graus máx -
                { 102, 1, "5,0 a 9,0" }, //pH 5 - 9
                { 139, 1, "1 mL/L" }, //Residuos Sedimentaveis 1 mL/L
                { 194, 1, "15 mg/L max" }, //Ferro Dissolvido
                { 219, 1, "1 mg/L max" }, //Manganês dissolvido
                { 220, 1, "1 mg/L max" }, //Cobre dissolvido
                { 162, 1, "5 mg/L max" }, //Zinco total
                { 143, 1, "20 mg/L max" }, //Óleo e Graxas
                { 136, 1, "120 mg/L máx" }, //Demanda bioquimica de oxigênio


                // Legislação 2: Resolução CONAMA nº 396/2008 (Águas Subterrâneas - Consumo
                // Humano)
                { 148, 2, "200 µg/L" }, // Alumínio [cite: 163]
                { 508, 2, "10 µg/L" }, // Arsênio [cite: 163]
                { 169, 2, "700 µg/L" }, // Bário [cite: 163]
                { 512, 2, "5 µg/L" }, // Cádmio [cite: 163]
                { 513, 2, "10 µg/L" }, // Chumbo [cite: 163]
                { 151, 2, "2.000 µg/L" }, // Cobre [cite: 163]
                { 517, 2, "50 µg/L" }, // Crômio [cite: 163]
                { 181, 2, "1.500 µg/L" }, // Fluoreto [cite: 163]
                { 510, 2, "1 µg/L" }, // Mercúrio [cite: 163]
                { 182, 2, "10.000 µg/L" }, // Nitrato [cite: 163]
                { 138, 2, "1.000.000 µg/L" }, // Sólidos Dissolvidos Totais [cite: 163]

                // Legislação 3: Resolução CONAMA nº 420/2009 (Investigação Água Subterrânea)
                { 508, 3, "10 µg/L" }, // Arsênio [cite: 453]
                { 169, 3, "700 µg/L" }, // Bário [cite: 453]
                { 512, 3, "5 µg/L" }, // Cádmio [cite: 453]
                { 513, 3, "10 µg/L" }, // Chumbo [cite: 453]
                { 510, 3, "1 µg/L" }, // Mercúrio [cite: 453]
                { 134, 3, "140 µg/L" }, // Fenol [cite: 455]
                { 163, 3, "500.000 µg/L" }, // Sódio (Xilenos ref.) [cite: 453]

                // Legislação 4: Resolução CONAMA nº 357/2005 (Águas Doces Classe 1)
                { 136, 4, "Até 3 mg/L" }, // DBO [cite: 1030]
                { 128, 4, "Não inferior a 6 mg/L" }, // Oxigênio Dissolvido [cite: 1031]
                { 141, 4, "Até 40 UNT" }, // Turbidez [cite: 1032]
                { 102, 4, "6,0 a 9,0" }, // pH [cite: 1034]
                { 138, 4, "500 mg/L" }, // Sólidos Dissolvidos Totais [cite: 1036]
                { 508, 4, "0,01 mg/L As" }, // Arsênio Total [cite: 1036]

                // Legislação 5: Consema Nº 181/2021 (Lançamento de Efluentes - Santa Catarina)

                //V1: -----------------------------
//                { 102, 5, "pH entre 6,0 e 9,0" }, // pH [cite: 758]
//                { 111, 5, "0,1 mg/L" }, // Cromo Hexavalente [cite: 766]
//                { 151, 5, "0,5 mg/L" }, // Cobre Total [cite: 767]
//                { 512, 5, "0,1 mg/L" }, // Cádmio Total [cite: 768]
//                { 510, 5, "0,005 mg/L" }, // Mercúrio Total [cite: 769]
//                { 162, 5, "1,0 mg/L" }, // Zinco Total [cite: 771]
//                { 508, 5, "0,1 mg/L" }, // Arsênio Total [cite: 772]
//                { 136, 5, "Máximo 60 mg/L DBO" }, // DBO [cite: 792]
//                { 143, 5, "Gorduras Animais: 30,0 mg/L" }, // Óleos e Graxas [cite: 764]
//                V1: ---------------------------------------------------------------

                //V2: ---------------------------
                { 102, 5, "5,0 a 9,0" }, //pH 5 - 9
                { 219, 5, "1 mg/L max" }, //Manganês dissolvido 1mg/L 219
                { 151, 5, "1 mg/L max" }, // Cobre Total  1mg/L
                { 162, 5, "1 mg/L max" }, // zinco total  1mg/L
                //V2: ---------------------------

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