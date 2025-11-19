package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.model.InspecaoPiezometroMovimento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inspecoes-piezometros-movimentos")
public class InspecaoPiezometroMovimentoController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<InspecaoPiezometroMovimento> getAllInspecoesMovimentos() {
        String sql = "SELECT FIRST 100 * FROM TB_INSPECAO_PIEZOMETRO_MVTO";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(InspecaoPiezometroMovimento.class));
    }
}