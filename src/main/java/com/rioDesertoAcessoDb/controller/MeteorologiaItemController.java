package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.model.MeteorologiaItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/meteorologia-itens")
public class MeteorologiaItemController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<MeteorologiaItem> getAllMeteorologiaItens() {
        String sql = "SELECT * FROM TB_METEOROLOGIA_ITEM";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(MeteorologiaItem.class));
    }
}