package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.model.CalibracaoEquipamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/calibracoes")
public class CalibracaoEquipamentoController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<CalibracaoEquipamento> getAllCalibracoes() {
        String sql = "SELECT * FROM TB_CALIBRACAO_EQUIPAMENTO";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CalibracaoEquipamento.class));
    }
}