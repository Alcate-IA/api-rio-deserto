package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.model.InspecaoPiezometro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inspecoes-piezometros")
public class InspecaoPiezometroController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<InspecaoPiezometro> getAllInspecoes() {
        String sql = "SELECT * FROM TB_INSPECAO_PIEZOMETRO";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(InspecaoPiezometro.class));
    }
}