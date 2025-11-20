package com.rioDesertoAcessoDb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController { //comentário 

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
                    LPAD(EXTRACT(MONTH FROM DT_INCLUSAO), 2, '0') || '/' || EXTRACT(YEAR FROM DT_INCLUSAO) AS MES_ANO,
                    AVG(VL_PRECIPITACAO) AS MEDIA_PRECIPITACAO
                 FROM TB_METEOROLOGIA_ITEM
                 GROUP BY EXTRACT(YEAR FROM DT_INCLUSAO), EXTRACT(MONTH FROM DT_INCLUSAO)
                ) m
            FULL JOIN 
                (SELECT 
                    LPAD(EXTRACT(MONTH FROM DT_INCLUSAO), 2, '0') || '/' || EXTRACT(YEAR FROM DT_INCLUSAO) AS MES_ANO,
                    AVG(VL_COTA) AS MEDIA_COTA
                 FROM TB_NIVEL_AGUA
                 GROUP BY EXTRACT(YEAR FROM DT_INCLUSAO), EXTRACT(MONTH FROM DT_INCLUSAO)
                ) n ON m.MES_ANO = n.MES_ANO
            FULL JOIN 
                (SELECT 
                    LPAD(EXTRACT(MONTH FROM DT_INCLUSAO), 2, '0') || '/' || EXTRACT(YEAR FROM DT_INCLUSAO) AS MES_ANO,
                    AVG(QT_NIVEL_ESTATICO) AS MEDIA_NIVEL_ESTATICO
                 FROM TB_INSPECAO_PIEZOMETRO_MVTO
                 GROUP BY EXTRACT(YEAR FROM DT_INCLUSAO), EXTRACT(MONTH FROM DT_INCLUSAO)
                ) p ON COALESCE(m.MES_ANO, n.MES_ANO) = p.MES_ANO
            ORDER BY 
                CAST(SUBSTRING(COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO) FROM 4 FOR 4) AS INTEGER) DESC,
                CAST(SUBSTRING(COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO) FROM 1 FOR 2) AS INTEGER) DESC
            """;

        return jdbcTemplate.queryForList(sql);
    }

    @GetMapping("/medias-mensais")
    public List<Map<String, Object>> getMediasMensais(
            @RequestParam String mesAnoInicio,
            @RequestParam String mesAnoFim) {

        int anoInicio = Integer.parseInt(mesAnoInicio.substring(3));
        int mesInicio = Integer.parseInt(mesAnoInicio.substring(0, 2));
        int anoFim = Integer.parseInt(mesAnoFim.substring(3));
        int mesFim = Integer.parseInt(mesAnoFim.substring(0, 2));

        String sql = """
        SELECT 
            COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO) AS mes_ano,
            m.MEDIA_PRECIPITACAO as precipitacao,
            n.MEDIA_COTA as cota_regua,
            p.MEDIA_NIVEL_ESTATICO as nivel_estatico
        FROM 
            (SELECT 
                LPAD(EXTRACT(MONTH FROM DT_INCLUSAO), 2, '0') || '/' || EXTRACT(YEAR FROM DT_INCLUSAO) AS MES_ANO,
                AVG(VL_PRECIPITACAO) AS MEDIA_PRECIPITACAO
             FROM TB_METEOROLOGIA_ITEM
             WHERE (EXTRACT(YEAR FROM DT_INCLUSAO) > ? OR 
                   (EXTRACT(YEAR FROM DT_INCLUSAO) = ? AND EXTRACT(MONTH FROM DT_INCLUSAO) >= ?))
               AND (EXTRACT(YEAR FROM DT_INCLUSAO) < ? OR 
                   (EXTRACT(YEAR FROM DT_INCLUSAO) = ? AND EXTRACT(MONTH FROM DT_INCLUSAO) <= ?))
             GROUP BY EXTRACT(YEAR FROM DT_INCLUSAO), EXTRACT(MONTH FROM DT_INCLUSAO)
            ) m
        FULL JOIN 
            (SELECT 
                LPAD(EXTRACT(MONTH FROM DT_INCLUSAO), 2, '0') || '/' || EXTRACT(YEAR FROM DT_INCLUSAO) AS MES_ANO,
                AVG(VL_COTA) AS MEDIA_COTA
             FROM TB_NIVEL_AGUA
             WHERE (EXTRACT(YEAR FROM DT_INCLUSAO) > ? OR 
                   (EXTRACT(YEAR FROM DT_INCLUSAO) = ? AND EXTRACT(MONTH FROM DT_INCLUSAO) >= ?))
               AND (EXTRACT(YEAR FROM DT_INCLUSAO) < ? OR 
                   (EXTRACT(YEAR FROM DT_INCLUSAO) = ? AND EXTRACT(MONTH FROM DT_INCLUSAO) <= ?))
             GROUP BY EXTRACT(YEAR FROM DT_INCLUSAO), EXTRACT(MONTH FROM DT_INCLUSAO)
            ) n ON m.MES_ANO = n.MES_ANO
        FULL JOIN 
            (SELECT 
                LPAD(EXTRACT(MONTH FROM DT_INCLUSAO), 2, '0') || '/' || EXTRACT(YEAR FROM DT_INCLUSAO) AS MES_ANO,
                AVG(QT_NIVEL_ESTATICO) AS MEDIA_NIVEL_ESTATICO
             FROM TB_INSPECAO_PIEZOMETRO_MVTO
             WHERE (EXTRACT(YEAR FROM DT_INCLUSAO) > ? OR 
                   (EXTRACT(YEAR FROM DT_INCLUSAO) = ? AND EXTRACT(MONTH FROM DT_INCLUSAO) >= ?))
               AND (EXTRACT(YEAR FROM DT_INCLUSAO) < ? OR 
                   (EXTRACT(YEAR FROM DT_INCLUSAO) = ? AND EXTRACT(MONTH FROM DT_INCLUSAO) <= ?))
             GROUP BY EXTRACT(YEAR FROM DT_INCLUSAO), EXTRACT(MONTH FROM DT_INCLUSAO)
            ) p ON COALESCE(m.MES_ANO, n.MES_ANO) = p.MES_ANO
        ORDER BY 
            CAST(SUBSTRING(COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO) FROM 4 FOR 4) AS INTEGER) DESC,
            CAST(SUBSTRING(COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO) FROM 1 FOR 2) AS INTEGER) DESC
        """;

        return jdbcTemplate.queryForList(sql,
                anoInicio-1, anoInicio, mesInicio, anoFim+1, anoFim, mesFim,
                anoInicio-1, anoInicio, mesInicio, anoFim+1, anoFim, mesFim,
                anoInicio-1, anoInicio, mesInicio, anoFim+1, anoFim, mesFim
        );
    }

    @PostMapping("/medias-mensais")
    public List<Map<String, Object>> getMediasMensaisPost(@RequestBody FiltroPeriodo filtro) {

        int anoInicio = Integer.parseInt(filtro.getMesAnoInicio().substring(3));
        int mesInicio = Integer.parseInt(filtro.getMesAnoInicio().substring(0, 2));
        int anoFim = Integer.parseInt(filtro.getMesAnoFim().substring(3));
        int mesFim = Integer.parseInt(filtro.getMesAnoFim().substring(0, 2));

        String sql = """
        SELECT 
            COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO) AS mes_ano,
            m.MEDIA_PRECIPITACAO as precipitacao,
            n.MEDIA_COTA as cota_regua,
            p.MEDIA_NIVEL_ESTATICO as nivel_estatico
        FROM 
            (SELECT 
                LPAD(EXTRACT(MONTH FROM DT_INCLUSAO), 2, '0') || '/' || EXTRACT(YEAR FROM DT_INCLUSAO) AS MES_ANO,
                AVG(VL_PRECIPITACAO) AS MEDIA_PRECIPITACAO
             FROM TB_METEOROLOGIA_ITEM
             WHERE (EXTRACT(YEAR FROM DT_INCLUSAO) > ? OR 
                   (EXTRACT(YEAR FROM DT_INCLUSAO) = ? AND EXTRACT(MONTH FROM DT_INCLUSAO) >= ?))
               AND (EXTRACT(YEAR FROM DT_INCLUSAO) < ? OR 
                   (EXTRACT(YEAR FROM DT_INCLUSAO) = ? AND EXTRACT(MONTH FROM DT_INCLUSAO) <= ?))
             GROUP BY EXTRACT(YEAR FROM DT_INCLUSAO), EXTRACT(MONTH FROM DT_INCLUSAO)
            ) m
        FULL JOIN 
            (SELECT 
                LPAD(EXTRACT(MONTH FROM DT_INCLUSAO), 2, '0') || '/' || EXTRACT(YEAR FROM DT_INCLUSAO) AS MES_ANO,
                AVG(VL_COTA) AS MEDIA_COTA
             FROM TB_NIVEL_AGUA
             WHERE (EXTRACT(YEAR FROM DT_INCLUSAO) > ? OR 
                   (EXTRACT(YEAR FROM DT_INCLUSAO) = ? AND EXTRACT(MONTH FROM DT_INCLUSAO) >= ?))
               AND (EXTRACT(YEAR FROM DT_INCLUSAO) < ? OR 
                   (EXTRACT(YEAR FROM DT_INCLUSAO) = ? AND EXTRACT(MONTH FROM DT_INCLUSAO) <= ?))
             GROUP BY EXTRACT(YEAR FROM DT_INCLUSAO), EXTRACT(MONTH FROM DT_INCLUSAO)
            ) n ON m.MES_ANO = n.MES_ANO
        FULL JOIN 
            (SELECT 
                LPAD(EXTRACT(MONTH FROM DT_INCLUSAO), 2, '0') || '/' || EXTRACT(YEAR FROM DT_INCLUSAO) AS MES_ANO,
                AVG(QT_NIVEL_ESTATICO) AS MEDIA_NIVEL_ESTATICO
             FROM TB_INSPECAO_PIEZOMETRO_MVTO
             WHERE (EXTRACT(YEAR FROM DT_INCLUSAO) > ? OR 
                   (EXTRACT(YEAR FROM DT_INCLUSAO) = ? AND EXTRACT(MONTH FROM DT_INCLUSAO) >= ?))
               AND (EXTRACT(YEAR FROM DT_INCLUSAO) < ? OR 
                   (EXTRACT(YEAR FROM DT_INCLUSAO) = ? AND EXTRACT(MONTH FROM DT_INCLUSAO) <= ?))
             GROUP BY EXTRACT(YEAR FROM DT_INCLUSAO), EXTRACT(MONTH FROM DT_INCLUSAO)
            ) p ON COALESCE(m.MES_ANO, n.MES_ANO) = p.MES_ANO
        ORDER BY 
            CAST(SUBSTRING(COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO) FROM 4 FOR 4) AS INTEGER) DESC,
            CAST(SUBSTRING(COALESCE(m.MES_ANO, n.MES_ANO, p.MES_ANO) FROM 1 FOR 2) AS INTEGER) DESC
        """;

        return jdbcTemplate.queryForList(sql,
                anoInicio-1, anoInicio, mesInicio, anoFim+1, anoFim, mesFim,
                anoInicio-1, anoInicio, mesInicio, anoFim+1, anoFim, mesFim,
                anoInicio-1, anoInicio, mesInicio, anoFim+1, anoFim, mesFim
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