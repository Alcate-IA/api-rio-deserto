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
}