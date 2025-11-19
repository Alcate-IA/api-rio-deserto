package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.model.InspecaoPiezometroFrequencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inspecoes-piezometros-frequencias")
public class InspecaoPiezometroFrequenciaController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<InspecaoPiezometroFrequencia> getAllInspecoesFrequencias() {
        String sql = "SELECT * FROM TB_INSPECAO_PIEZOMETRO_FREQ";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(InspecaoPiezometroFrequencia.class));
    }
}