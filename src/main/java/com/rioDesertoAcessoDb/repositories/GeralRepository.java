package com.rioDesertoAcessoDb.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GeralRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Long getContadoresZeus() {
        String sql = "SELECT count(*) FROM tb_piezometro WHERE cd_empresa = 18 and fg_situacao = 'A'";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class);
        } catch (Exception e) {
            return 0L;
        }
    }

    public Long getContadoresRdLab() {
        String sql = "SELECT count(ide.*) FROM identificacao ide " +
                "JOIN tb_piezometro pie ON pie.cd_piezometro = ide.id_zeus";
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
                "ipm.qt_nivel_estatico as leitura, ipm.ds_observacao, ipm.nm_colaborador_inspecao as colaborador, " +
                "p.cd_piezometro, p.cd_empresa, p.id_piezometro, p.nm_piezometro, p.tp_piezometro " +
                "FROM tb_inspecao_piezometro_mvto ipm " +
                "JOIN tb_inspecao_piezometro ip on ipm.cd_inspecao_piezometro = ip.cd_inspecao_piezometro " +
                "JOIN tb_piezometro p ON ip.cd_piezometro = p.cd_piezometro " +
                "WHERE P.cd_empresa = 18 AND P.fg_situacao = 'A' " +
                "UNION ALL " +
                "SELECT 'NIVEL_AGUA' as origem, NAI.cd_nivel_agua_item as id, NAI.dt_inspecao, " +
                "NAI.qt_nivel_estatico as leitura, NAI.ds_observacao, NAI.nm_colaborador_inspecao as colaborador, " +
                "p.cd_piezometro, p.cd_empresa, p.id_piezometro, p.nm_piezometro, p.tp_piezometro " +
                "FROM tb_nivel_agua_item NAI " +
                "JOIN tb_nivel_agua NA on NAI.cd_nivel_agua = NA.cd_nivel_agua " +
                "JOIN tb_piezometro P ON p.cd_piezometro = na.cd_piezometro " +
                "WHERE p.cd_empresa = 18 and P.fg_situacao = 'A' " +
                "UNION ALL " +
                "SELECT 'RECURSOS_HIDRICOS' as origem, RHI.cd_recursos_hidricos_item as id, RHI.dt_inspecao, " +
                "RHI.qt_leitura as leitura, CAST(NULL AS CHAR) as ds_observacao, RHI.nm_colaborador_inspecao as colaborador, "
                +
                "p.cd_piezometro, p.cd_empresa, p.id_piezometro, p.nm_piezometro, p.tp_piezometro " +
                "FROM tb_recursos_hidricos_item RHI " +
                "JOIN tb_recursos_hidricos RI ON RHI.cd_recursos_hidricos = RI.cd_recursos_hidricos " +
                "JOIN tb_piezometro p ON RI.cd_piezometro = P.cd_piezometro " +
                "WHERE p.cd_empresa = 18 " +
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
}