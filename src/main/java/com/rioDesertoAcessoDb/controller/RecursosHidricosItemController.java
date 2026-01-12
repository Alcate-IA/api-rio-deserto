package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.model.RecursosHidricosItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
@RestController
@RequestMapping("/recursos-hidricos-itens")
public class RecursosHidricosItemController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<RecursosHidricosItem> getAllRecursosHidricosItens() {
        String sql = "SELECT FIRST 100 * FROM TB_RECURSOS_HIDRICOS_ITEM";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RecursosHidricosItem.class));
    }

    
}