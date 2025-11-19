package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.model.RecursosHidricos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/recursos-hidricos")
public class RecursosHidricosController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<RecursosHidricos> getAllRecursosHidricos() {
        String sql = "SELECT * FROM TB_RECURSOS_HIDRICOS";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RecursosHidricos.class));
    }
}