package com.rioDesertoAcessoDb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    @GetMapping("/piezometro/{cdPiezometro}")
    public List<Map<String, Object>> getDadosPiezometro(@PathVariable Integer cdPiezometro) {
        String sql = """
            SELECT 
                ip.qt_cota_superficie AS cota_superficie,
                ip.qt_cota_base AS cota_base,
                COALESCE(p.mes_ano, v.mes_ano, n.mes_ano) AS mes_ano,
                p.precipitacao_total AS precipitacao,
                v.vazao_bombeamento AS vazao_bombeamento,
                n.media_nivel_estatico AS nivel_estatico
            FROM 
                (SELECT 
                    DATE_TRUNC('month', dt_item)::date AS mes_ano,
                    SUM(vl_precipitacao) AS precipitacao_total
                 FROM tb_meteorologia_item
                 GROUP BY DATE_TRUNC('month', dt_item)
                ) p
            FULL JOIN 
                (SELECT 
                    mes_ano_vazao AS mes_ano,
                    vazao_bombeamento
                 FROM tb_vazao_mina
                ) v ON p.mes_ano = v.mes_ano
            FULL JOIN 
                (SELECT 
                    DATE_TRUNC('month', ipm.dt_inclusao)::date AS mes_ano,
                    AVG(ipm.qt_nivel_estatico) AS media_nivel_estatico
                 FROM tb_inspecao_piezometro_mvto ipm
                 INNER JOIN tb_inspecao_piezometro ip ON ipm.cd_inspecao_piezometro = ip.cd_inspecao_piezometro
                 WHERE ip.cd_piezometro = ?
                 GROUP BY DATE_TRUNC('month', ipm.dt_inclusao)
                ) n ON COALESCE(p.mes_ano, v.mes_ano) = n.mes_ano
            CROSS JOIN (
                SELECT qt_cota_superficie, qt_cota_base 
                FROM tb_inspecao_piezometro 
                WHERE cd_piezometro = ?
                LIMIT 1
            ) ip
            ORDER BY COALESCE(p.mes_ano, v.mes_ano, n.mes_ano) DESC
            """;

        return jdbcTemplate.queryForList(sql, cdPiezometro, cdPiezometro);
    }

    @GetMapping("/piezometro/{cdPiezometro}/filtro")
    public List<Map<String, Object>> getDadosPiezometroComFiltro(
            @PathVariable Integer cdPiezometro,
            @RequestParam String mesAnoInicio,
            @RequestParam String mesAnoFim) {

        String dataInicio = "01/" + mesAnoInicio;
        String dataFim = "01/" + mesAnoFim;

        String sql = """
            SELECT 
                ip.qt_cota_superficie AS cota_superficie,
                ip.qt_cota_base AS cota_base,
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
                    DATE_TRUNC('month', ipm.dt_inclusao)::date AS mes_ano,
                    AVG(ipm.qt_nivel_estatico) AS media_nivel_estatico
                 FROM tb_inspecao_piezometro_mvto ipm
                 INNER JOIN tb_inspecao_piezometro ip ON ipm.cd_inspecao_piezometro = ip.cd_inspecao_piezometro
                 WHERE ip.cd_piezometro = ?
                   AND ipm.dt_inclusao >= TO_DATE(?, 'DD/MM/YYYY') 
                   AND ipm.dt_inclusao <= TO_DATE(?, 'DD/MM/YYYY')
                 GROUP BY DATE_TRUNC('month', ipm.dt_inclusao)
                ) n ON COALESCE(p.mes_ano, v.mes_ano) = n.mes_ano
            CROSS JOIN (
                SELECT qt_cota_superficie, qt_cota_base 
                FROM tb_inspecao_piezometro 
                WHERE cd_piezometro = ?
                LIMIT 1
            ) ip
            ORDER BY COALESCE(p.mes_ano, v.mes_ano, n.mes_ano) DESC
            """;

        return jdbcTemplate.queryForList(sql,
                dataInicio, dataFim,
                dataInicio, dataFim,
                cdPiezometro,
                dataInicio, dataFim,
                cdPiezometro
        );
    }

    @GetMapping("/regua/{cdPiezometro}")
    public List<Map<String, Object>> getDadosRegua(@PathVariable Integer cdPiezometro) {
        String sql = """
        SELECT 
            na.vl_cota AS cota_superficie,
            COALESCE(p.mes_ano, v.mes_ano, n.mes_ano) AS mes_ano,
            p.precipitacao_total AS precipitacao,
            v.vazao_bombeamento AS vazao_bombeamento,
            n.media_nivel_estatico AS nivel_estatico
        FROM 
            (SELECT 
                DATE_TRUNC('month', dt_item)::date AS mes_ano,
                SUM(vl_precipitacao) AS precipitacao_total
             FROM tb_meteorologia_item
             GROUP BY DATE_TRUNC('month', dt_item)
            ) p
        FULL JOIN 
            (SELECT 
                mes_ano_vazao AS mes_ano,
                vazao_bombeamento
             FROM tb_vazao_mina
            ) v ON p.mes_ano = v.mes_ano
        FULL JOIN 
            (SELECT 
                DATE_TRUNC('month', nai.dt_inclusao)::date AS mes_ano,
                AVG(nai.qt_nivel_estatico) AS media_nivel_estatico
             FROM tb_nivel_agua_item nai
             INNER JOIN tb_nivel_agua na ON nai.cd_nivel_agua = na.cd_nivel_agua
             WHERE na.cd_piezometro = ?
             GROUP BY DATE_TRUNC('month', nai.dt_inclusao)
            ) n ON COALESCE(p.mes_ano, v.mes_ano) = n.mes_ano
        CROSS JOIN (
            SELECT vl_cota 
            FROM tb_nivel_agua 
            WHERE cd_piezometro = ?
            LIMIT 1
        ) na
        ORDER BY COALESCE(p.mes_ano, v.mes_ano, n.mes_ano) DESC
        """;

        return jdbcTemplate.queryForList(sql, cdPiezometro, cdPiezometro);
    }

    @GetMapping("/regua/{cdPiezometro}/filtro")
    public List<Map<String, Object>> getDadosReguaComFiltro(
            @PathVariable Integer cdPiezometro,
            @RequestParam String mesAnoInicio,
            @RequestParam String mesAnoFim) {

        String dataInicio = "01/" + mesAnoInicio;
        String dataFim = "01/" + mesAnoFim;

        String sql = """
        SELECT 
            na.vl_cota AS cota_superficie,
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
                DATE_TRUNC('month', nai.dt_inclusao)::date AS mes_ano,
                AVG(nai.qt_nivel_estatico) AS media_nivel_estatico
             FROM tb_nivel_agua_item nai
             INNER JOIN tb_nivel_agua na ON nai.cd_nivel_agua = na.cd_nivel_agua
             WHERE na.cd_piezometro = ?
               AND nai.dt_inclusao >= TO_DATE(?, 'DD/MM/YYYY') 
               AND nai.dt_inclusao <= TO_DATE(?, 'DD/MM/YYYY')
             GROUP BY DATE_TRUNC('month', nai.dt_inclusao)
            ) n ON COALESCE(p.mes_ano, v.mes_ano) = n.mes_ano
        CROSS JOIN (
            SELECT vl_cota 
            FROM tb_nivel_agua 
            WHERE cd_piezometro = ?
            LIMIT 1
        ) na
        ORDER BY COALESCE(p.mes_ano, v.mes_ano, n.mes_ano) DESC
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
}