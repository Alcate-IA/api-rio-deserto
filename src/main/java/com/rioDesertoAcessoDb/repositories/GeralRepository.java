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
}