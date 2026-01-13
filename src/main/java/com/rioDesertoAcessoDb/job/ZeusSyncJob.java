package com.rioDesertoAcessoDb.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

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
        // atribuirClassificacoesAleatorias();
        atribuirClassificacoesEspecificas();

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
    /*
     * private void atribuirClassificacoesAleatorias() {
     * System.out.
     * println("\n4. Atribuindo classificações aleatórias aos piezômetros ativos..."
     * );
     * long inicioClassificacao = System.currentTimeMillis();
     * 
     * try {
     * // IDs possíveis de classificação (1 a 4)
     * int[] classificacoesDisponiveis = { 1, 2, 3, 4 };
     * Random random = new Random();
     * 
     * // Buscar piezômetros ativos da empresa 18
     * String selectQuery =
     * "SELECT cd_piezometro FROM tb_piezometro WHERE fg_situacao = 'A' AND cd_empresa = '18'"
     * ;
     * List<Integer> piezometrosAtivos =
     * postgresJdbcTemplate.queryForList(selectQuery, Integer.class);
     * 
     * if (piezometrosAtivos.isEmpty()) {
     * System.out.println("   Nenhum piezômetro ativo encontrado para a empresa 18"
     * );
     * return;
     * }
     * 
     * System.out.println("   Piezômetros ativos encontrados: " +
     * piezometrosAtivos.size());
     * 
     * // Atualizar cada piezômetro com uma classificação aleatória
     * String updateQuery =
     * "UPDATE tb_piezometro SET id_classificacao_agua = ? WHERE cd_piezometro = ?";
     * int piezometrosAtualizados = 0;
     * 
     * for (Integer cdPiezometro : piezometrosAtivos) {
     * // Selecionar uma classificação aleatória
     * int classificacaoAleatoria = classificacoesDisponiveis[random
     * .nextInt(classificacoesDisponiveis.length)];
     * 
     * // Atualizar o piezômetro
     * int rowsAffected = postgresJdbcTemplate.update(updateQuery,
     * classificacaoAleatoria, cdPiezometro);
     * 
     * if (rowsAffected > 0) {
     * piezometrosAtualizados++;
     * }
     * }
     * 
     * long fimClassificacao = System.currentTimeMillis();
     * System.out.println("   Piezômetros atualizados com classificação: " +
     * piezometrosAtualizados);
     * System.out.println(
     * "   Tempo atribuição de classificações: " + (fimClassificacao -
     * inicioClassificacao) + "ms");
     * 
     * } catch (Exception e) {
     * System.err.println("   ERRO ao atribuir classificações aleatórias: " +
     * e.getMessage());
     * e.printStackTrace();
     * }
     * }
     */

    private void atribuirClassificacoesEspecificas() {
        System.out.println("\n4. Atribuindo classificações específicas aos piezômetros ativos...");
        long inicioClassificacao = System.currentTimeMillis();

        try {
            // Arrays de IDs para cada classificação
            int[] efluentes = { 858, 859, 860, 861, 862, 863, 876, 877, 878, 879 };
            int[] aguasSuperficiais = { 691, 883, 884, 856, 857, 885, 886, 887, 888, 889, 864, 865, 823, 824, 825, 826,
                    827, 828, 829, 830, 831, 832, 833, 834, 835, 836, 837, 838, 839, 840, 841, 842, 843, 844, 845, 890,
                    891, 906, 894 };
            int[] piezometrosAguasSubterraneas = { 313, 314, 84, 85, 86, 88, 93, 100, 109, 113, 121, 137, 140, 143, 145,
                    162, 163, 185, 590, 199, 206, 207, 208, 209, 211, 212, 213, 214, 215, 222, 232, 235, 238, 245, 247,
                    262, 263, 269, 275, 293, 294, 295, 296, 297, 298, 299, 300, 301, 302, 303, 304, 305, 306, 308, 309,
                    310, 311, 312, 315, 316, 317, 318, 319, 320, 321, 322, 323, 324, 325, 326, 327, 328, 329, 330, 331,
                    332, 333, 334, 335, 336, 337, 338, 339, 340, 341, 342, 343, 345, 346, 347, 349, 350, 351, 352, 353,
                    355, 356, 357, 358, 359, 360, 361, 362, 364, 365, 366, 367, 369, 372, 373, 378, 379, 380, 381, 382,
                    383, 384, 385, 386, 387, 388, 389, 390, 391, 392, 393, 395, 396, 397, 399, 400, 401, 403, 404, 407,
                    408, 409, 410, 411, 412, 413, 414, 415, 416, 417, 420, 425, 427, 428, 433, 435, 438, 440, 441, 447,
                    450, 451, 452, 453, 454, 455, 456, 457, 459, 460, 461, 462, 463, 464, 466, 467, 476, 477, 478, 479,
                    480, 481, 482, 483, 484, 485, 486, 487, 488, 489, 491, 492, 493, 495, 497, 498, 501, 502, 503, 504,
                    506, 507, 591, 574, 575, 576, 577, 578, 579, 580, 581, 582, 583, 584, 585, 586, 587, 736, 738, 681,
                    683, 688, 689, 661, 665, 692, 662, 663, 664, 693, 695, 698, 700, 701, 702, 658, 597, 598, 599, 600,
                    601, 602, 603, 604, 605, 606, 607, 608, 609, 610, 611, 612, 613, 614, 615, 616, 617, 618, 619, 620,
                    621, 622, 623, 624, 625, 626, 627, 629, 630, 631, 632, 633, 634, 635, 636, 637, 638, 639, 640, 641,
                    642, 643, 644, 645, 646, 647, 648, 649, 650, 651, 652, 653, 654, 655, 656, 657, 704, 705, 706, 707,
                    708, 709, 710, 711, 712, 713, 717, 718, 719, 721, 722, 723, 724, 726, 727, 728, 729, 733, 734, 735,
                    752, 753, 754, 755, 756, 757, 758, 759, 760, 761, 762, 763, 764, 765, 766, 850, 791, 792, 793, 794,
                    795, 796, 797, 798, 851, 852, 799, 800, 801, 802, 803, 804, 904, 854, 855, 866, 867, 868, 869, 870,
                    871, 872, 873, 874, 875, 907 };

            Set<Integer> setEfluentes = Arrays.stream(efluentes).boxed().collect(Collectors.toSet());
            Set<Integer> setAguasSuperficiais = Arrays.stream(aguasSuperficiais).boxed().collect(Collectors.toSet());
            Set<Integer> setPiezometrosAguasSubterraneas = Arrays.stream(piezometrosAguasSubterraneas).boxed()
                    .collect(Collectors.toSet());

            String selectQuery = "SELECT cd_piezometro FROM tb_piezometro WHERE fg_situacao = 'A' AND cd_empresa = '18'";
            List<Integer> piezometrosAtivos = postgresJdbcTemplate.queryForList(selectQuery, Integer.class);

            if (piezometrosAtivos.isEmpty()) {
                System.out.println("   Nenhum piezômetro ativo encontrado para a empresa 18");
                return;
            }

            System.out.println("   Piezômetros ativos encontrados: " + piezometrosAtivos.size());

            String updateQuery = "UPDATE tb_piezometro SET id_classificacao_agua = ? WHERE cd_piezometro = ?";
            int piezometrosAtualizados = 0;
            int efluentesAtribuidos = 0;
            int aguasSuperficiaisAtribuidas = 0;
            int aguasSubterraneasAtribuidas = 0;
            int semClassificacao = 0;

            for (Integer cdPiezometro : piezometrosAtivos) {
                Integer idClassificacao = null;

                if (setEfluentes.contains(cdPiezometro)) {
                    idClassificacao = 1; // Efluentes
                    efluentesAtribuidos++;
                } else if (setAguasSuperficiais.contains(cdPiezometro)) {
                    idClassificacao = 4; // Águas superficiais
                    aguasSuperficiaisAtribuidas++;
                } else if (setPiezometrosAguasSubterraneas.contains(cdPiezometro)) {
                    idClassificacao = 2; // Piezômetros águas subterrâneas
                    aguasSubterraneasAtribuidas++;
                } else {
                    semClassificacao++;
                    continue;
                }

                // Atualizar o piezômetro
                int rowsAffected = postgresJdbcTemplate.update(updateQuery, idClassificacao, cdPiezometro);

                if (rowsAffected > 0) {
                    piezometrosAtualizados++;
                }
            }

            long fimClassificacao = System.currentTimeMillis();
            System.out.println("   Resumo das atribuições:");
            System.out.println("   - Efluentes atribuídos: " + efluentesAtribuidos);
            System.out.println("   - Águas superficiais atribuídas: " + aguasSuperficiaisAtribuidas);
            System.out.println("   - Águas subterrâneas atribuídas: " + aguasSubterraneasAtribuidas);
            System.out.println("   - Piezômetros sem classificação específica: " + semClassificacao);
            System.out.println("   - Total de piezômetros atualizados: " + piezometrosAtualizados);
            System.out.println(
                    "   Tempo atribuição de classificações: " + (fimClassificacao - inicioClassificacao) + "ms");

        } catch (Exception e) {
            System.err.println("   ERRO ao atribuir classificações específicas: " + e.getMessage());
            e.printStackTrace();
        }
    }

}