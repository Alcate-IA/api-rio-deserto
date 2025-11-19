package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.model.NivelAguaItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/nivel-agua-itens")
public class NivelAguaItemController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<NivelAguaItem> getAllNivelAguaItens() {
        String sql = "SELECT * FROM TB_NIVEL_AGUA_ITEM";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(NivelAguaItem.class));
    }
}