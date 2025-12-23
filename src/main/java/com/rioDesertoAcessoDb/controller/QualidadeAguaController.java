package com.rioDesertoAcessoDb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/qualidade-agua")
@Tag(name = "Qualidade da Água", description = "APIs para consulta de dados de qualidade da água")
public class QualidadeAguaController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/historico-completo/{idZeus}")
    @Operation(summary = "Obter histórico completo de coleta", description = "" +
            "\n <br> Retorna um relatório completo com todas as amostras e análises químicas para um ponto Zeus sem filtros de período ou análises. ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Histórico completo retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Ponto Zeus não encontrado")
    })
    public Map<String, Object> getHistoricoCompletoPorZeus(@PathVariable Integer idZeus) {
        String sqlAmostras = """
                SELECT
                    aq.N_REGISTRO,
                    aq.DATA,
                    ide.id_zeus,
                    ide.identificacao
                FROM amostra_quimico aq
                INNER JOIN identificacao ide ON (aq.identificacao = ide.codigo)
                WHERE ide.ID_ZEUS = ?
                ORDER BY aq.DATA DESC, aq.N_REGISTRO DESC
                """;

        List<Map<String, Object>> amostras = jdbcTemplate.queryForList(sqlAmostras, idZeus);

        List<Map<String, Object>> resultadoCompleto = new ArrayList<>();

        for (Map<String, Object> amostra : amostras) {
            Long nRegistro = ((Number) amostra.get("N_REGISTRO")).longValue();

            String sqlInfoAmostra = """
                    SELECT
                        aq.data,
                        aq.identificacao,
                        aq.coletor,
                        aq.tipoamostra,
                        ide.identificacao as nome_identificacao,
                        ide.id_zeus
                    FROM amostra_quimico aq
                    LEFT JOIN identificacao ide ON aq.identificacao = ide.codigo
                    WHERE aq.N_REGISTRO = ?
                    """;

            Map<String, Object> infoAmostra = Collections.emptyMap();
            try {
                infoAmostra = jdbcTemplate.queryForMap(sqlInfoAmostra, nRegistro);
            } catch (Exception e) {
                infoAmostra = new HashMap<>();
                infoAmostra.put("erro", "Informações não encontradas");
            }

            String sqlAnalises = """
                    SELECT
                        aaq.simbolo,
                        a.nome as nome_analise,
                        aaq.resultado
                    FROM amostraanalise_quimico aaq
                    JOIN analise a ON aaq.simbolo = a.simbolo
                    WHERE aaq.N_REGISTRO = ?
                    ORDER BY aaq.simbolo
                    """;

            List<Map<String, Object>> analises = jdbcTemplate.queryForList(sqlAnalises, nRegistro);

            Map<String, Object> resultadoAmostra = new LinkedHashMap<>();
            resultadoAmostra.put("nRegistro", nRegistro);
            resultadoAmostra.put("informacoesAmostra", infoAmostra);
            resultadoAmostra.put("analises", analises);
            resultadoAmostra.put("totalAnalises", analises.size());

            resultadoCompleto.add(resultadoAmostra);
        }

        Map<String, Object> respostaFinal = new LinkedHashMap<>();
        respostaFinal.put("idZeus", idZeus);
        respostaFinal.put("totalAmostras", resultadoCompleto.size());
        respostaFinal.put("amostras", resultadoCompleto);

        return respostaFinal;
    }

    @PostMapping("/coleta-completa/filtro-analises")
    @Operation(summary = "Obter relatório completo de coleta", description = "" +
            "\n <br> Retorna um relatório completo com todas as amostras e análises químicas para um ponto Zeus dentro de um período. "
            +
            "\n <br> Você pode filtrar por uma lista de IDs de análise específicos através do campo 'filtros'.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Relatório completo retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Ponto Zeus não encontrado")
    })
    public Map<String, Object> getRelatorioCompletoPost(@RequestBody ColetaCompletaRequest request) {
        Integer idZeus = request.getIdZeus();
        String mesAnoInicio = request.getMesAnoInicio();
        String mesAnoFim = request.getMesAnoFim();
        List<Long> filtros = request.getFiltros();

        String dataInicio = "01/" + mesAnoInicio;
        String dataFim = "01/" + mesAnoFim;

        // Buscar classificação da água e legislações associadas
        String sqlLegislacao = """
                SELECT
                    c.nome_classificacao,
                    l.id_legislacao,
                    l.nome_legislacao
                FROM tb_piezometro p
                JOIN tipo_classificacao_agua_piezometro c ON p.id_classificacao_agua = c.id_classificacao
                JOIN classificacao_legislacao cl ON c.id_classificacao = cl.id_classificacao
                JOIN legislacoes l ON cl.id_legislacao = l.id_legislacao
                WHERE p.cd_piezometro = ?
                """;

        List<Map<String, Object>> legislacoesRows = jdbcTemplate.queryForList(sqlLegislacao, idZeus);

        String nomeClassificacao = null;
        Map<String, Object> legislacoesMap = new LinkedHashMap<>();

        if (!legislacoesRows.isEmpty()) {
            nomeClassificacao = (String) legislacoesRows.get(0).get("nome_classificacao");

            for (Map<String, Object> row : legislacoesRows) {
                Integer idLegislacao = (Integer) row.get("id_legislacao");
                String nomeLegislacao = (String) row.get("nome_legislacao");

                // Filtro para parâmetros de legislação
                String sqlParametros;
                Object[] paramsP;
                if (filtros != null && !filtros.isEmpty()) {
                    String placeholders = String.join(",", Collections.nCopies(filtros.size(), "?"));
                    sqlParametros = String.format("""
                            SELECT
                                a.simbolo,
                                a.nome as nome_analise,
                                pl.parametro
                            FROM parametros_legislacao pl
                            JOIN analise a ON pl.id_analise = a.id_analise
                            WHERE pl.id_legislacao = ?
                            AND a.id_analise IN (%s)
                            """, placeholders);
                    List<Object> paramListP = new ArrayList<>();
                    paramListP.add(idLegislacao);
                    paramListP.addAll(filtros);
                    paramsP = paramListP.toArray();
                } else {
                    sqlParametros = """
                            SELECT
                                a.simbolo,
                                a.nome as nome_analise,
                                pl.parametro
                            FROM parametros_legislacao pl
                            JOIN analise a ON pl.id_analise = a.id_analise
                            WHERE pl.id_legislacao = ?
                            """;
                    paramsP = new Object[] { idLegislacao };
                }

                List<Map<String, Object>> parametrosList = jdbcTemplate.queryForList(sqlParametros, paramsP);
                List<Map<String, String>> paramsFormatados = new ArrayList<>();

                for (Map<String, Object> param : parametrosList) {
                    Map<String, String> p = new LinkedHashMap<>();
                    p.put("simbolo", (String) param.get("simbolo"));
                    p.put("nome_analise", (String) param.get("nome_analise"));
                    p.put("parametro", (String) param.get("parametro"));
                    paramsFormatados.add(p);
                }

                // Só adiciona a legislação se ela tiver parâmetros (caso filtrado)
                if (!paramsFormatados.isEmpty()) {
                    Map<String, Object> detalhesLegislacao = new LinkedHashMap<>();
                    detalhesLegislacao.put("parametros_legislacao", paramsFormatados);
                    legislacoesMap.put(nomeLegislacao, detalhesLegislacao);
                }
            }
        }

        String sqlAmostras = """
                SELECT
                    aq.N_REGISTRO,
                    aq.DATA,
                    ide.id_zeus,
                    ide.identificacao
                FROM amostra_quimico aq
                INNER JOIN identificacao ide ON (aq.identificacao = ide.codigo)
                WHERE ide.ID_ZEUS = ?
                  AND aq.DATA >= TO_DATE(?, 'DD/MM/YYYY')
                  AND aq.DATA <= TO_DATE(?, 'DD/MM/YYYY')
                ORDER BY aq.N_REGISTRO
                """;

        List<Map<String, Object>> amostras = jdbcTemplate.queryForList(sqlAmostras, idZeus, dataInicio, dataFim);

        List<Map<String, Object>> resultadoCompleto = new ArrayList<>();

        for (Map<String, Object> amostra : amostras) {
            Long nRegistro = ((Number) amostra.get("N_REGISTRO")).longValue();

            String sqlInfoAmostra = """
                    SELECT
                        aq.data,
                        aq.identificacao,
                        aq.coletor,
                        aq.tipoamostra,
                        ide.identificacao as nome_identificacao,
                        ide.id_zeus
                    FROM amostra_quimico aq
                    LEFT JOIN identificacao ide ON aq.identificacao = ide.codigo
                    WHERE aq.N_REGISTRO = ?
                    """;

            Map<String, Object> infoAmostra = Collections.emptyMap();
            try {
                infoAmostra = jdbcTemplate.queryForMap(sqlInfoAmostra, nRegistro);
            } catch (Exception e) {
                infoAmostra = new HashMap<>();
                infoAmostra.put("erro", "Informações não encontradas");
            }

            // Aplicar filtro de análises se fornecido
            String sqlAnalises;
            Object[] paramsA;
            if (filtros != null && !filtros.isEmpty()) {
                String placeholders = String.join(",", Collections.nCopies(filtros.size(), "?"));
                sqlAnalises = String.format("""
                        SELECT
                            aaq.simbolo,
                            a.nome as nome_analise,
                            aaq.resultado
                        FROM amostraanalise_quimico aaq
                        JOIN analise a ON aaq.simbolo = a.simbolo
                        WHERE aaq.N_REGISTRO = ?
                        AND a.id_analise IN (%s)
                        ORDER BY aaq.simbolo
                        """, placeholders);

                List<Object> paramListA = new ArrayList<>();
                paramListA.add(nRegistro);
                paramListA.addAll(filtros);
                paramsA = paramListA.toArray();
            } else {
                sqlAnalises = """
                        SELECT
                            aaq.simbolo,
                            a.nome as nome_analise,
                            aaq.resultado
                        FROM amostraanalise_quimico aaq
                        JOIN analise a ON aaq.simbolo = a.simbolo
                        WHERE aaq.N_REGISTRO = ?
                        ORDER BY aaq.simbolo
                        """;
                paramsA = new Object[] { nRegistro };
            }

            List<Map<String, Object>> analises = jdbcTemplate.queryForList(sqlAnalises, paramsA);

            Map<String, Object> resultadoAmostra = new LinkedHashMap<>();
            resultadoAmostra.put("nRegistro", nRegistro);
            resultadoAmostra.put("informacoesAmostra", infoAmostra);
            resultadoAmostra.put("analises", analises);
            resultadoAmostra.put("totalAnalises", analises.size());

            resultadoCompleto.add(resultadoAmostra);
        }

        Map<String, Object> respostaFinal = new LinkedHashMap<>();
        respostaFinal.put("idZeus", idZeus);
        respostaFinal.put("periodoInicio", dataInicio);
        respostaFinal.put("periodoFim", dataFim);
        respostaFinal.put("totalAmostras", resultadoCompleto.size());

        if (nomeClassificacao != null) {
            respostaFinal.put("classificao_agua", nomeClassificacao);
            respostaFinal.put("legislacoes", legislacoesMap);
        }

        respostaFinal.put("amostras", resultadoCompleto);

        return respostaFinal;
    }

    public static class ColetaCompletaRequest {
        private Integer idZeus;
        private String mesAnoInicio;
        private String mesAnoFim;
        private List<Long> filtros;

        public ColetaCompletaRequest() {
        }

        public Integer getIdZeus() {
            return idZeus;
        }

        public void setIdZeus(Integer idZeus) {
            this.idZeus = idZeus;
        }

        public String getMesAnoInicio() {
            return mesAnoInicio;
        }

        public void setMesAnoInicio(String mesAnoInicio) {
            this.mesAnoInicio = mesAnoInicio;
        }

        public String getMesAnoFim() {
            return mesAnoFim;
        }

        public void setMesAnoFim(String mesAnoFim) {
            this.mesAnoFim = mesAnoFim;
        }

        public List<Long> getFiltros() {
            return filtros;
        }

        public void setFiltros(List<Long> filtros) {
            this.filtros = filtros;
        }
    }
}
