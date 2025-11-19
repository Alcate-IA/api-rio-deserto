package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.model.NivelAgua;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/niveis-agua")
public class NivelAguaController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<NivelAgua> getAllNiveisAgua() {
        String sql = "SELECT * FROM TB_NIVEL_AGUA";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(NivelAgua.class));
    }
}