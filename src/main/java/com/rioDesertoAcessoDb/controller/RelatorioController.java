package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.repositories.PiezometroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PiezometroRepository piezometroRepository;

    @GetMapping("/medias-mensais-todos")
    public List<Map<String, Object>> getMediasMensais() {
        String sql = """
            SELECT 
                COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO, v.MES_ANO) AS mes_ano,
                m.MEDIA_PRECIPITACAO as precipitacao,
                n.MEDIA_COTA as cota_regua,
                p.MEDIA_NIVEL_ESTATICO as nivel_estatico,
                v.VAZAO_BOMBEAMENTO as vazao_bombeamento
            FROM 
                (SELECT 
                    TO_CHAR(DT_INCLUSAO, 'MM/YYYY') AS MES_ANO,
                    AVG(VL_PRECIPITACAO) AS MEDIA_PRECIPITACAO
                 FROM TB_METEOROLOGIA_ITEM
                 GROUP BY TO_CHAR(DT_INCLUSAO, 'MM/YYYY'), EXTRACT(YEAR FROM DT_INCLUSAO), EXTRACT(MONTH FROM DT_INCLUSAO)
                ) m
            FULL JOIN 
                (SELECT 
                    TO_CHAR(DT_INCLUSAO, 'MM/YYYY') AS MES_ANO,
                    AVG(QT_NIVEL_ESTATICO) AS MEDIA_COTA
                 FROM TB_NIVEL_AGUA_ITEM
                 GROUP BY TO_CHAR(DT_INCLUSAO, 'MM/YYYY'), EXTRACT(YEAR FROM DT_INCLUSAO), EXTRACT(MONTH FROM DT_INCLUSAO)
                ) n ON m.MES_ANO = n.MES_ANO
            FULL JOIN 
                (SELECT 
                    TO_CHAR(DT_INCLUSAO, 'MM/YYYY') AS MES_ANO,
                    AVG(QT_NIVEL_ESTATICO) AS MEDIA_NIVEL_ESTATICO
                 FROM TB_INSPECAO_PIEZOMETRO_MVTO
                 GROUP BY TO_CHAR(DT_INCLUSAO, 'MM/YYYY'), EXTRACT(YEAR FROM DT_INCLUSAO), EXTRACT(MONTH FROM DT_INCLUSAO)
                ) p ON COALESCE(m.MES_ANO, n.MES_ANO) = p.MES_ANO
            FULL JOIN
                (SELECT 
                    TO_CHAR(mes_ano_vazao, 'MM/YYYY') AS MES_ANO,
                    AVG(vazao_bombeamento) AS VAZAO_BOMBEAMENTO
                 FROM TB_VAZAO_MINA
                 GROUP BY TO_CHAR(mes_ano_vazao, 'MM/YYYY'), EXTRACT(YEAR FROM mes_ano_vazao), EXTRACT(MONTH FROM mes_ano_vazao)
                ) v ON COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO) = v.MES_ANO
            ORDER BY 
                TO_DATE(COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO, v.MES_ANO), 'MM/YYYY') DESC
            """;

        return jdbcTemplate.queryForList(sql);
    }

    @PostMapping("/medias-mensais")
    public List<Map<String, Object>> getMediasMensaisPost(@RequestBody FiltroPeriodo filtro) {

        String dataInicio = "01/" + filtro.getMesAnoInicio();
        String dataFim = "01/" + filtro.getMesAnoFim();

        String sql = """
        SELECT 
            COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO, v.MES_ANO) AS mes_ano,
            m.MEDIA_PRECIPITACAO as precipitacao,
            n.MEDIA_COTA as cota_regua,
            p.MEDIA_NIVEL_ESTATICO as nivel_estatico,
            v.VAZAO_BOMBEAMENTO as vazao_bombeamento
        FROM 
            (SELECT 
                TO_CHAR(DT_INCLUSAO, 'MM/YYYY') AS MES_ANO,
                AVG(VL_PRECIPITACAO) AS MEDIA_PRECIPITACAO
             FROM TB_METEOROLOGIA_ITEM
             WHERE DT_INCLUSAO >= TO_DATE(?, 'DD/MM/YYYY') 
               AND DT_INCLUSAO <= (DATE_TRUNC('MONTH', TO_DATE(?, 'DD/MM/YYYY')) + INTERVAL '1 MONTH - 1 day')
             GROUP BY TO_CHAR(DT_INCLUSAO, 'MM/YYYY'), EXTRACT(YEAR FROM DT_INCLUSAO), EXTRACT(MONTH FROM DT_INCLUSAO)
            ) m
        FULL JOIN 
            (SELECT 
                TO_CHAR(DT_INCLUSAO, 'MM/YYYY') AS MES_ANO,
                AVG(QT_NIVEL_ESTATICO) AS MEDIA_COTA
             FROM TB_NIVEL_AGUA_ITEM
             WHERE DT_INCLUSAO >= TO_DATE(?, 'DD/MM/YYYY') 
               AND DT_INCLUSAO <= (DATE_TRUNC('MONTH', TO_DATE(?, 'DD/MM/YYYY')) + INTERVAL '1 MONTH - 1 day')
             GROUP BY TO_CHAR(DT_INCLUSAO, 'MM/YYYY'), EXTRACT(YEAR FROM DT_INCLUSAO), EXTRACT(MONTH FROM DT_INCLUSAO)
            ) n ON m.MES_ANO = n.MES_ANO
        FULL JOIN 
            (SELECT 
                TO_CHAR(DT_INCLUSAO, 'MM/YYYY') AS MES_ANO,
                AVG(QT_NIVEL_ESTATICO) AS MEDIA_NIVEL_ESTATICO
             FROM TB_INSPECAO_PIEZOMETRO_MVTO
             WHERE DT_INCLUSAO >= TO_DATE(?, 'DD/MM/YYYY') 
               AND DT_INCLUSAO <= (DATE_TRUNC('MONTH', TO_DATE(?, 'DD/MM/YYYY')) + INTERVAL '1 MONTH - 1 day')
             GROUP BY TO_CHAR(DT_INCLUSAO, 'MM/YYYY'), EXTRACT(YEAR FROM DT_INCLUSAO), EXTRACT(MONTH FROM DT_INCLUSAO)
            ) p ON COALESCE(m.MES_ANO, n.MES_ANO) = p.MES_ANO
        FULL JOIN
            (SELECT 
                TO_CHAR(mes_ano_vazao, 'MM/YYYY') AS MES_ANO,
                AVG(vazao_bombeamento) AS VAZAO_BOMBEAMENTO
             FROM TB_VAZAO_MINA
             WHERE mes_ano_vazao >= TO_DATE(?, 'DD/MM/YYYY') 
               AND mes_ano_vazao <= (DATE_TRUNC('MONTH', TO_DATE(?, 'DD/MM/YYYY')) + INTERVAL '1 MONTH - 1 day')
             GROUP BY TO_CHAR(mes_ano_vazao, 'MM/YYYY'), EXTRACT(YEAR FROM mes_ano_vazao), EXTRACT(MONTH FROM mes_ano_vazao)
            ) v ON COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO) = v.MES_ANO
        ORDER BY 
            TO_DATE(COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO, v.MES_ANO), 'MM/YYYY') DESC
        """;

        return jdbcTemplate.queryForList(sql,
                dataInicio, dataFim,
                dataInicio, dataFim,
                dataInicio, dataFim,
                dataInicio, dataFim
        );
    }

    //lembrar de passar assim: http://localhost:8080/relatorios/medias-mensais?mesAnoInicio=01/2023&mesAnoFim=12/2023
    @GetMapping("/medias-mensais")
    public List<Map<String, Object>> getMediasMensais(
            @RequestParam String mesAnoInicio,
            @RequestParam String mesAnoFim) {

        String dataInicio = "01/" + mesAnoInicio;
        String dataFim = "01/" + mesAnoFim;

        String sql = """
        SELECT 
            COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO, v.MES_ANO) AS mes_ano,
            m.MEDIA_PRECIPITACAO as precipitacao,
            n.MEDIA_COTA as cota_regua,
            p.MEDIA_NIVEL_ESTATICO as nivel_estatico,
            v.VAZAO_BOMBEAMENTO as vazao_bombeamento
        FROM 
            (SELECT 
                TO_CHAR(DT_INCLUSAO, 'MM/YYYY') AS MES_ANO,
                AVG(VL_PRECIPITACAO) AS MEDIA_PRECIPITACAO
             FROM TB_METEOROLOGIA_ITEM
             WHERE DT_INCLUSAO >= TO_DATE(?, 'DD/MM/YYYY') 
               AND DT_INCLUSAO <= (DATE_TRUNC('MONTH', TO_DATE(?, 'DD/MM/YYYY')) + INTERVAL '1 MONTH - 1 day')
             GROUP BY TO_CHAR(DT_INCLUSAO, 'MM/YYYY'), EXTRACT(YEAR FROM DT_INCLUSAO), EXTRACT(MONTH FROM DT_INCLUSAO)
            ) m
        FULL JOIN 
            (SELECT 
                TO_CHAR(DT_INCLUSAO, 'MM/YYYY') AS MES_ANO,
                AVG(QT_NIVEL_ESTATICO) AS MEDIA_COTA
             FROM TB_NIVEL_AGUA_ITEM
             WHERE DT_INCLUSAO >= TO_DATE(?, 'DD/MM/YYYY') 
               AND DT_INCLUSAO <= (DATE_TRUNC('MONTH', TO_DATE(?, 'DD/MM/YYYY')) + INTERVAL '1 MONTH - 1 day')
             GROUP BY TO_CHAR(DT_INCLUSAO, 'MM/YYYY'), EXTRACT(YEAR FROM DT_INCLUSAO), EXTRACT(MONTH FROM DT_INCLUSAO)
            ) n ON m.MES_ANO = n.MES_ANO
        FULL JOIN 
            (SELECT 
                TO_CHAR(DT_INCLUSAO, 'MM/YYYY') AS MES_ANO,
                AVG(QT_NIVEL_ESTATICO) AS MEDIA_NIVEL_ESTATICO
             FROM TB_INSPECAO_PIEZOMETRO_MVTO
             WHERE DT_INCLUSAO >= TO_DATE(?, 'DD/MM/YYYY') 
               AND DT_INCLUSAO <= (DATE_TRUNC('MONTH', TO_DATE(?, 'DD/MM/YYYY')) + INTERVAL '1 MONTH - 1 day')
             GROUP BY TO_CHAR(DT_INCLUSAO, 'MM/YYYY'), EXTRACT(YEAR FROM DT_INCLUSAO), EXTRACT(MONTH FROM DT_INCLUSAO)
            ) p ON COALESCE(m.MES_ANO, n.MES_ANO) = p.MES_ANO
        FULL JOIN
            (SELECT 
                TO_CHAR(mes_ano_vazao, 'MM/YYYY') AS MES_ANO,
                AVG(vazao_bombeamento) AS VAZAO_BOMBEAMENTO
             FROM TB_VAZAO_MINA
             WHERE mes_ano_vazao >= TO_DATE(?, 'DD/MM/YYYY') 
               AND mes_ano_vazao <= (DATE_TRUNC('MONTH', TO_DATE(?, 'DD/MM/YYYY')) + INTERVAL '1 MONTH - 1 day')
             GROUP BY TO_CHAR(mes_ano_vazao, 'MM/YYYY'), EXTRACT(YEAR FROM mes_ano_vazao), EXTRACT(MONTH FROM mes_ano_vazao)
            ) v ON COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO) = v.MES_ANO
        ORDER BY 
            TO_DATE(COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO, v.MES_ANO), 'MM/YYYY') DESC
        """;

        return jdbcTemplate.queryForList(sql,
                dataInicio, dataFim,
                dataInicio, dataFim,
                dataInicio, dataFim,
                dataInicio, dataFim
        );
    }


    //http://localhost:8080/relatorios/piezometro/5/filtro?mesAnoInicio=01/2024&mesAnoFim=12/2024
    @GetMapping("/piezometro/{cdPiezometro}/filtro")
    public List<Map<String, Object>> getDadosPiezometroComFiltro(
            @PathVariable Integer cdPiezometro,
            @RequestParam String mesAnoInicio,
            @RequestParam String mesAnoFim) {

        // Verifica o tipo do piezômetro
        String tipoPiezometro = piezometroRepository.findTipoPiezometroById(cdPiezometro);

        if ("PR".equals(tipoPiezometro)) {
            return getDadosReguaComFiltro(cdPiezometro, mesAnoInicio, mesAnoFim);
        } else if ("PC".equals(tipoPiezometro) || "PV".equals(tipoPiezometro)) {
            return getDadosRecursosHidricosComFiltro(cdPiezometro, mesAnoInicio, mesAnoFim);
        } else if ("PP".equals(tipoPiezometro)) {
            return getDadosPiezometroComumComFiltro(cdPiezometro, mesAnoInicio, mesAnoFim);
        } else {
            throw new IllegalArgumentException("Tipo de piezômetro não suportado: " + tipoPiezometro);
        }
    }

    //PR (Régua)
    private List<Map<String, Object>> getDadosReguaComFiltro(Integer cdPiezometro, String mesAnoInicio, String mesAnoFim) {
        String dataInicio = "01/" + mesAnoInicio;
        String dataFim = "01/" + mesAnoFim;

        String sql = """
    SELECT 
        na.vl_cota AS cota_superficie,
        NULL AS cota_base,  -- PR não tem cota_base, então retorna NULL
        COALESCE(p.mes_ano, v.mes_ano, n.mes_ano) AS mes_ano,
        p.precipitacao_total AS precipitacao,
        v.vazao_bombeamento AS vazao_bombeamento,
        n.media_nivel_estatico AS nivel_estatico
    FROM 
        (SELECT 
            DATE_TRUNC('month', dt_item)::date AS mes_ano,
            SUM(vl_precipitacao) AS precipitacao_total 
         FROM tb_meteorologia_item
         WHERE dt_item >= TO_DATE(?, 'DD/MM/YYYY') 
           AND dt_item <= TO_DATE(?, 'DD/MM/YYYY')
           AND cd_meteorologia = 12
         GROUP BY DATE_TRUNC('month', dt_item)
        ) p
    FULL JOIN 
        (SELECT 
            mes_ano_vazao AS mes_ano,
            vazao_bombeamento
         FROM tb_vazao_mina
         WHERE mes_ano_vazao >= TO_DATE(?, 'DD/MM/YYYY') 
           AND mes_ano_vazao <= TO_DATE(?, 'DD/MM/YYYY')
        ) v ON p.mes_ano = v.mes_ano
    FULL JOIN 
        (SELECT 
            DATE_TRUNC('month', nai.dt_inspecao)::date AS mes_ano,  -- 1. CORRIGIDO: Usando dt_inspecao
            AVG(nai.qt_nivel_estatico) AS media_nivel_estatico
         FROM tb_nivel_agua_item nai
         INNER JOIN tb_nivel_agua na ON nai.cd_nivel_agua = na.cd_nivel_agua
         WHERE na.cd_piezometro = ?
           AND nai.dt_inspecao >= TO_DATE(?, 'DD/MM/YYYY') -- 2. CORRIGIDO: Filtro por dt_inspecao
           AND nai.dt_inspecao <= TO_DATE(?, 'DD/MM/YYYY') -- 3. CORRIGIDO: Filtro por dt_inspecao
         GROUP BY DATE_TRUNC('month', nai.dt_inspecao) -- 4. CORRIGIDO: Agrupando por dt_inspecao
        ) n ON COALESCE(p.mes_ano, v.mes_ano) = n.mes_ano
    CROSS JOIN (
        SELECT vl_cota 
        FROM tb_nivel_agua 
        WHERE cd_piezometro = ?
        LIMIT 1
    ) na
    ORDER BY COALESCE(p.mes_ano, v.mes_ano, n.mes_ano) ASC
    """;

        return jdbcTemplate.queryForList(sql,
                dataInicio, dataFim,
                dataInicio, dataFim,
                cdPiezometro,
                dataInicio, dataFim,
                cdPiezometro
        );
    }

    // PC (Calhas) e PV (Ponto de Vazão)
    private List<Map<String, Object>> getDadosRecursosHidricosComFiltro(Integer cdPiezometro, String mesAnoInicio, String mesAnoFim) {
        String dataInicio = "01/" + mesAnoInicio;
        String dataFim = "01/" + mesAnoFim;

        String sql = """
        SELECT 
            NULL AS cota_superficie,
            NULL AS cota_base,
            COALESCE(p.mes_ano, v.mes_ano, rh.mes_ano) AS mes_ano,
            p.precipitacao_total AS precipitacao,
            v.vazao_bombeamento AS vazao_bombeamento,
            rh.vazao_calha AS vazao_calha,
            NULL AS nivel_estatico
        FROM 
            (SELECT 
                DATE_TRUNC('month', dt_item)::date AS mes_ano,
                SUM(vl_precipitacao) AS precipitacao_total
             FROM tb_meteorologia_item
             WHERE dt_item >= TO_DATE(?, 'DD/MM/YYYY') 
               AND dt_item <= TO_DATE(?, 'DD/MM/YYYY') 
               AND cd_meteorologia = 12
             GROUP BY DATE_TRUNC('month', dt_item)
            ) p
        FULL JOIN 
            (SELECT 
                mes_ano_vazao AS mes_ano,
                vazao_bombeamento
             FROM tb_vazao_mina
             WHERE mes_ano_vazao >= TO_DATE(?, 'DD/MM/YYYY') 
               AND mes_ano_vazao <= TO_DATE(?, 'DD/MM/YYYY')
            ) v ON p.mes_ano = v.mes_ano
        FULL JOIN 
            (SELECT 
                DATE_TRUNC('month', rhi.dt_inspecao)::date AS mes_ano,
                AVG(rhi.qt_leitura) AS vazao_calha
             FROM tb_recursos_hidricos_item rhi
             INNER JOIN tb_recursos_hidricos rh ON rhi.cd_recursos_hidricos = rh.cd_recursos_hidricos
             WHERE rh.cd_piezometro = ?
               AND rhi.dt_inspecao >= TO_DATE(?, 'DD/MM/YYYY')
               AND rhi.dt_inspecao <= TO_DATE(?, 'DD/MM/YYYY')
             GROUP BY DATE_TRUNC('month', rhi.dt_inspecao)
            ) rh ON COALESCE(p.mes_ano, v.mes_ano) = rh.mes_ano
        ORDER BY COALESCE(p.mes_ano, v.mes_ano, rh.mes_ano) ASC
        """;

        return jdbcTemplate.queryForList(sql,
                dataInicio, dataFim,
                dataInicio, dataFim,
                cdPiezometro,
                dataInicio, dataFim
        );
    }

    // PP (Piezômetro de Profundidade)
    private List<Map<String, Object>> getDadosPiezometroComumComFiltro(Integer cdPiezometro, String mesAnoInicio, String mesAnoFim) {
        String dataInicio = "01/" + mesAnoInicio;
        String dataFim = "01/" + mesAnoFim;

        String sql = """
        SELECT 
            ip.qt_cota_superficie AS cota_superficie,
            ip.qt_cota_base AS cota_base,
            COALESCE(p.mes_ano, v.mes_ano, n.mes_ano) AS mes_ano,
            p.precipitacao_total AS precipitacao,
            v.vazao_bombeamento AS vazao_bombeamento,
            n.media_nivel_estatico AS nivel_estatico,
            NULL AS vazao_calha
        FROM 
            (SELECT 
                DATE_TRUNC('month', dt_item)::date AS mes_ano,
                SUM(vl_precipitacao) AS precipitacao_total
             FROM tb_meteorologia_item
             WHERE dt_item >= TO_DATE(?, 'DD/MM/YYYY') 
               AND dt_item <= TO_DATE(?, 'DD/MM/YYYY') 
               AND cd_meteorologia = 12
             GROUP BY DATE_TRUNC('month', dt_item)
            ) p
        FULL JOIN 
            (SELECT 
                mes_ano_vazao AS mes_ano,
                vazao_bombeamento
             FROM tb_vazao_mina
             WHERE mes_ano_vazao >= TO_DATE(?, 'DD/MM/YYYY') 
               AND mes_ano_vazao <= TO_DATE(?, 'DD/MM/YYYY')
            ) v ON p.mes_ano = v.mes_ano
        FULL JOIN 
            (SELECT 
                DATE_TRUNC('month', ipm.dt_inspecao)::date AS mes_ano,
                AVG(ipm.qt_nivel_estatico) AS media_nivel_estatico
             FROM tb_inspecao_piezometro_mvto ipm
             INNER JOIN tb_inspecao_piezometro ip ON ipm.cd_inspecao_piezometro = ip.cd_inspecao_piezometro
             WHERE ip.cd_piezometro = ?
               AND ipm.dt_inspecao >= TO_DATE(?, 'DD/MM/YYYY')
               AND ipm.dt_inspecao <= TO_DATE(?, 'DD/MM/YYYY')
             GROUP BY DATE_TRUNC('month', ipm.dt_inspecao)
            ) n ON COALESCE(p.mes_ano, v.mes_ano) = n.mes_ano
        CROSS JOIN (
            SELECT qt_cota_superficie, qt_cota_base 
            FROM tb_inspecao_piezometro 
            WHERE cd_piezometro = ?
            LIMIT 1
        ) ip
        ORDER BY COALESCE(p.mes_ano, v.mes_ano, n.mes_ano) ASC
        """;

        return jdbcTemplate.queryForList(sql,
                dataInicio, dataFim,
                dataInicio, dataFim,
                cdPiezometro,
                dataInicio, dataFim,
                cdPiezometro
        );
    }

    public static class FiltroPeriodo {
        private String mesAnoInicio;
        private String mesAnoFim;

        public FiltroPeriodo() {
        }

        public FiltroPeriodo(String mesAnoInicio, String mesAnoFim) {
            this.mesAnoInicio = mesAnoInicio;
            this.mesAnoFim = mesAnoFim;
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
    }

    //http://localhost:8080/relatorios/coleta/206/filtro?mesAnoInicio=01/2010&mesAnoFim=12/2023
    @GetMapping("coleta/{idZeus}/filtro")
    public List<Map<String, Object>> getRelatorioPorZeus(
            @PathVariable Integer idZeus,
            @RequestParam String mesAnoInicio,
            @RequestParam String mesAnoFim) {

        String dataInicio = "01/" + mesAnoInicio;
        String dataFim = "01/" + mesAnoFim;

        String sql = """
            SELECT 
                aq.N_REGISTRO, 
                aq.DATA, 
                ide.id_zeus,
                ide.identificacao,
                COUNT(*) as total_registros
            FROM amostra_quimico aq
            INNER JOIN identificacao ide ON (aq.identificacao = ide.codigo)
            LEFT JOIN amostraanalise_quimico aaq ON (aq.n_registro = aaq.n_registro) 
            WHERE ide.ID_ZEUS = ?
              AND aq.DATA >= TO_DATE(?, 'DD/MM/YYYY') 
              AND aq.DATA <= TO_DATE(?, 'DD/MM/YYYY')
            GROUP BY aq.N_REGISTRO, aq.DATA, ide.id_zeus, ide.identificacao
            ORDER BY aq.N_REGISTRO
            """;

        return jdbcTemplate.queryForList(sql, idZeus, dataInicio, dataFim);
    }

    private static final Map<String, String> MAPEAMENTO_ABREVIATURAS = Map.ofEntries(
            Map.entry("pH", "pH"),
            Map.entry("pH.", "pH"),
            Map.entry("Ac", "acidez"),
            Map.entry("S.D.", "solidosDissolvidos"),
            Map.entry("R.S.", "residuosSedimentaveis"),
            Map.entry("S.T.", "solidosTotais"),
            Map.entry("SO4", "sulfato"),
            Map.entry("Cond", "condutividade"),
            Map.entry("Fe T", "ferroTotal"),
            Map.entry("Fe Dis.", "ferroDissolvido"),
            Map.entry("Mn", "manganes"),
            Map.entry("Zn", "zinco"),
            Map.entry("Cu Dis.", "cobreDissolvido"),
            Map.entry("Al Dis.", "aluminioDissolvido")
    );

    @GetMapping("coleta/analises-quimicas/{nRegistro}")
    public Map<String, Object> getAnalisesQuimicasSimplificadas(@PathVariable Long nRegistro) {

        String sql = "SELECT simbolo, resultado FROM amostraanalise_quimico WHERE N_REGISTRO = ? ORDER BY simbolo";
        List<Map<String, Object>> dadosBrutos = jdbcTemplate.queryForList(sql, nRegistro);

        Map<String, String> analisesMap = new LinkedHashMap<>();
        for (Map<String, Object> linha : dadosBrutos) {
            String simbolo = (String) linha.get("simbolo");
            String resultado = (String) linha.get("resultado");

            String nomeCamelCase = MAPEAMENTO_ABREVIATURAS.getOrDefault(simbolo, simbolo);
            analisesMap.put(nomeCamelCase, resultado);
        }

        String sqlAmostra = """
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

        Map<String, Object> infoAmostra;
        try {
            infoAmostra = jdbcTemplate.queryForMap(sqlAmostra, nRegistro);

            Map<String, Object> infoCamelCase = new LinkedHashMap<>();
            for (Map.Entry<String, Object> entry : infoAmostra.entrySet()) {
                String keyCamelCase = toCamelCase(entry.getKey());
                infoCamelCase.put(keyCamelCase, entry.getValue());
            }
            infoAmostra = infoCamelCase;

        } catch (Exception e) {
            infoAmostra = new HashMap<>();
            infoAmostra.put("erro", "Informacoes da amostra nao encontradas");
        }

        Map<String, Object> resposta = new LinkedHashMap<>();
        resposta.put("nRegistro", nRegistro);
        resposta.put("informacoesAmostra", infoAmostra);
        resposta.put("totalParametros", analisesMap.size());
        resposta.put("analises", analisesMap);

        return resposta;
    }

    private String toCamelCase(String snakeCase) {
        if (snakeCase == null || snakeCase.isEmpty()) {
            return snakeCase;
        }

        String[] parts = snakeCase.split("_");
        StringBuilder camelCase = new StringBuilder(parts[0].toLowerCase());

        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (!part.isEmpty()) {
                camelCase.append(Character.toUpperCase(part.charAt(0)))
                        .append(part.substring(1).toLowerCase());
            }
        }

        return camelCase.toString();
    }
}