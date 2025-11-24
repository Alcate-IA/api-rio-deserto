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
                COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO) AS mes_ano,
                m.MEDIA_PRECIPITACAO as precipitacao,
                n.MEDIA_COTA as cota_regua,
                p.MEDIA_NIVEL_ESTATICO as nivel_estatico
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
            ORDER BY 
                TO_DATE(COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO), 'MM/YYYY') DESC
            """;

        return jdbcTemplate.queryForList(sql);
    }

    @PostMapping("/medias-mensais")
    public List<Map<String, Object>> getMediasMensaisPost(@RequestBody FiltroPeriodo filtro) {

        String dataInicio = "01/" + filtro.getMesAnoInicio();
        String dataFim = "01/" + filtro.getMesAnoFim();

        String sql = """
        SELECT 
            COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO) AS mes_ano,
            m.MEDIA_PRECIPITACAO as precipitacao,
            n.MEDIA_COTA as cota_regua,
            p.MEDIA_NIVEL_ESTATICO as nivel_estatico
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
        ORDER BY 
            TO_DATE(COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO), 'MM/YYYY') DESC
        """;

        return jdbcTemplate.queryForList(sql,
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
            COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO) AS mes_ano,
            m.MEDIA_PRECIPITACAO as precipitacao,
            n.MEDIA_COTA as cota_regua,
            p.MEDIA_NIVEL_ESTATICO as nivel_estatico
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
        ORDER BY 
            TO_DATE(COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO), 'MM/YYYY') DESC
        """;

        return jdbcTemplate.queryForList(sql,
                dataInicio, dataFim,
                dataInicio, dataFim,
                dataInicio, dataFim
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