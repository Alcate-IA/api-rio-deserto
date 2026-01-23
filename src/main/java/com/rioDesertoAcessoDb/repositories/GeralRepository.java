package com.rioDesertoAcessoDb.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GeralRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Long getContadoresZeusTotais() {
        String sql = "SELECT count(*) FROM tb_piezometro WHERE cd_empresa = 18";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class);
        } catch (Exception e) {
            return 0L;
        }
    }

    public Long getContadoresRdLabTotais() {
        String sql = "SELECT count(ide.*) FROM identificacao ide " +
                "JOIN tb_piezometro pie ON pie.cd_piezometro = ide.id_zeus WHERE pie.cd_empresa = 18";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class);
        } catch (Exception e) {
            return 0L;
        }
    }

    public Long getContadoresZeusAtivos() {
        String sql = "SELECT count(*) FROM tb_piezometro WHERE cd_empresa = 18 and fg_situacao = 'A'";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class);
        } catch (Exception e) {
            return 0L;
        }
    }

    public Long getContadoresRdLabAtivos() {
        String sql = "SELECT count(ide.*) FROM identificacao ide " +
                "JOIN tb_piezometro pie ON pie.cd_piezometro = ide.id_zeus WHERE pie.cd_empresa = 18 and pie.fg_situacao = 'A'";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class);
        } catch (Exception e) {
            return 0L;
        }
    }

    public Long getContadoresZeusInativos() {
        String sql = "SELECT count(*) FROM tb_piezometro WHERE cd_empresa = 18 and fg_situacao = 'I'";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class);
        } catch (Exception e) {
            return 0L;
        }
    }

    public Long getContadoresRdLabInativos() {
        String sql = "SELECT count(ide.*) FROM identificacao ide " +
                "JOIN tb_piezometro pie ON pie.cd_piezometro = ide.id_zeus WHERE pie.cd_empresa = 18 and pie.fg_situacao = 'I'";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class);
        } catch (Exception e) {
            return 0L;
        }
    }

    public java.util.List<java.util.Map<String, Object>> getUltimosMovimentos() {
        String sql = "SELECT AQ.data, AQ.identificacao, AQ.coletor, AQ.n_registro, " +
                "IDE.codigo, IDE.identificacao, IDE.id_zeus, " +
                "PIE.cd_piezometro, PIE.nm_piezometro " +
                "from amostra_quimico AQ " +
                "JOIN identificacao IDE ON AQ.identificacao = IDE.codigo " +
                "JOIN tb_piezometro PIE ON PIE.cd_piezometro = IDE.id_zeus " +
                "WHERE PIE.cd_empresa = 18 AND PIE.fg_situacao = 'A' " +
                "ORDER BY AQ.data DESC " +
                "LIMIT 10";
        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            return new java.util.ArrayList<>();
        }
    }

    public java.util.List<java.util.Map<String, Object>> getUltimosMovimentosSistema() {
        String sql = "SELECT * FROM (" +
                "SELECT 'INSPECAO_PIEZOMETRO' as origem, ipm.cd_inspecao_piezometro_movto as id, ipm.dt_inspecao, " +
                "ipm.qt_nivel_estatico as nivel_estatico, CAST(NULL AS DOUBLE PRECISION) as vazao, " +
                "ipm.ds_observacao, ipm.nm_colaborador_inspecao as colaborador, " +
                "p.cd_piezometro, p.cd_empresa, p.id_piezometro, p.nm_piezometro, p.tp_piezometro " +
                "FROM tb_inspecao_piezometro_mvto ipm " +
                "JOIN tb_inspecao_piezometro ip on ipm.cd_inspecao_piezometro = ip.cd_inspecao_piezometro " +
                "JOIN tb_piezometro p ON ip.cd_piezometro = p.cd_piezometro " +
                "WHERE P.cd_empresa = 18 AND P.fg_situacao = 'A' " +
                "UNION ALL " +
                "SELECT 'NIVEL_AGUA' as origem, NAI.cd_nivel_agua_item as id, NAI.dt_inspecao, " +
                "NAI.qt_nivel_estatico as nivel_estatico, CAST(NULL AS DOUBLE PRECISION) as vazao, " +
                "NAI.ds_observacao, NAI.nm_colaborador_inspecao as colaborador, " +
                "p.cd_piezometro, p.cd_empresa, p.id_piezometro, p.nm_piezometro, p.tp_piezometro " +
                "FROM tb_nivel_agua_item NAI " +
                "JOIN tb_nivel_agua NA on NAI.cd_nivel_agua = NA.cd_nivel_agua " +
                "JOIN tb_piezometro P ON p.cd_piezometro = na.cd_piezometro " +
                "WHERE p.cd_empresa = 18 and P.fg_situacao = 'A' " +
                "UNION ALL " +
                "SELECT 'RECURSOS_HIDRICOS' as origem, rhi.cd_recursos_hidricos_item as id, rhi.dt_inspecao, " +
                "CAST(NULL AS DOUBLE PRECISION) as nivel_estatico, rhi.qt_leitura as vazao, " +
                "rhi.ds_observacao, rhi.nm_colaborador_inspecao as colaborador, " +
                "p.cd_piezometro, p.cd_empresa, p.id_piezometro, p.nm_piezometro, p.tp_piezometro " +
                "FROM tb_recursos_hidricos_item rhi " +
                "JOIN tb_recursos_hidricos rh ON rhi.cd_recursos_hidricos = rh.cd_recursos_hidricos " +
                "JOIN tb_piezometro p ON p.cd_piezometro = rh.cd_piezometro " +
                "WHERE p.cd_empresa = 18 AND p.fg_situacao = 'A' " +
                ") as combined_results " +
                "ORDER BY dt_inspecao DESC " +
                "LIMIT 10";
        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    public java.util.Map<String, Object> getEstatisticasPiezometro(Integer cdPiezometro, String origem) {
        String sql = "";
        if ("INSPECAO_PIEZOMETRO".equals(origem)) {
            sql = "SELECT AVG(ipm.qt_nivel_estatico) as media, " +
                    "MAX(ipm.qt_nivel_estatico) as maior_leitura, " +
                    "MIN(ipm.qt_nivel_estatico) as menor_leitura " +
                    "FROM tb_inspecao_piezometro_mvto ipm " +
                    "JOIN tb_inspecao_piezometro ip ON ipm.cd_inspecao_piezometro = ip.cd_inspecao_piezometro " +
                    "WHERE ip.cd_piezometro = ?";
        } else if ("NIVEL_AGUA".equals(origem)) {
            sql = "SELECT AVG(nai.qt_nivel_estatico) as media, " +
                    "MAX(nai.qt_nivel_estatico) as maior_leitura, " +
                    "MIN(nai.qt_nivel_estatico) as menor_leitura " +
                    "FROM tb_nivel_agua_item nai " +
                    "JOIN tb_nivel_agua na ON nai.cd_nivel_agua = na.cd_nivel_agua " +
                    "WHERE na.cd_piezometro = ?";
        } else if ("RECURSOS_HIDRICOS".equals(origem)) {
            sql = "SELECT AVG(rhi.qt_leitura) as media, " +
                    "MAX(rhi.qt_leitura) as maior_leitura, " +
                    "MIN(rhi.qt_leitura) as menor_leitura " +
                    "FROM tb_recursos_hidricos_item rhi " +
                    "JOIN tb_recursos_hidricos rh ON rhi.cd_recursos_hidricos = rh.cd_recursos_hidricos " +
                    "WHERE rh.cd_piezometro = ?";
        } else {
            return new java.util.HashMap<>();
        }

        try {
            return jdbcTemplate.queryForMap(sql, cdPiezometro);
        } catch (Exception e) {
            return new java.util.HashMap<>();
        }
    }

    public java.util.List<java.util.Map<String, Object>> getDadosVazaoXPrecipitacao() {
        String sql = """
                SELECT
                    v.mes_ano_vazao AS mes_ano,
                    v.vazao_bombeamento AS vazao_bombeamento,
                    p.precipitacao_total AS precipitacao
                FROM
                    tb_vazao_mina v
                LEFT JOIN (
                    SELECT
                        DATE_TRUNC('month', dt_item)::date AS mes_ano,
                        SUM(vl_precipitacao) AS precipitacao_total
                    FROM tb_meteorologia_item
                    WHERE cd_meteorologia = 12
                    GROUP BY DATE_TRUNC('month', dt_item)
                ) p ON DATE_TRUNC('month', v.mes_ano_vazao)::date = p.mes_ano
                ORDER BY v.mes_ano_vazao ASC
                """;
        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }
}