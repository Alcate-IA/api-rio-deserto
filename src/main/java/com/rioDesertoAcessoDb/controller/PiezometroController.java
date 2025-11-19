package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.model.Piezometro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/piezometros")
public class PiezometroController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<Piezometro> getAllPiezometros() {
        String sql = "SELECT * FROM TB_PIEZOMETRO";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Piezometro.class));
    }
}