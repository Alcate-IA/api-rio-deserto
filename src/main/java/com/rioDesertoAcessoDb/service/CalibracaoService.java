package com.rioDesertoAcessoDb.service;

import com.rioDesertoAcessoDb.model.CalibracaoEquipamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalibracaoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<CalibracaoEquipamento> findAll() {
        String sql = "SELECT * FROM TB_CALIBRACAO_EQUIPAMENTO ORDER BY CD_CALIBRACAO_EQUIPAMENTO";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CalibracaoEquipamento.class));
    }

    public CalibracaoEquipamento findById(Integer id) {
        String sql = "SELECT * FROM TB_CALIBRACAO_EQUIPAMENTO WHERE CD_CALIBRACAO_EQUIPAMENTO = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(CalibracaoEquipamento.class), id);
    }

    public List<CalibracaoEquipamento> findBySituacao(String situacao) {
        String sql = "SELECT * FROM TB_CALIBRACAO_EQUIPAMENTO WHERE FG_SITUACAO = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CalibracaoEquipamento.class), situacao);
    }
}