package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.model.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<Pessoa> getAllPessoas() {
        String sql = "SELECT FIRST 100 * FROM TB_PESSOA";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Pessoa.class));
    }
}