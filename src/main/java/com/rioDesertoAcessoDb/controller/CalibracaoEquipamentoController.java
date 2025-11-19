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

    // GET todas as calibrações
    @GetMapping
    public List<CalibracaoEquipamento> getAllCalibracoes() {
        String sql = "SELECT * FROM TB_CALIBRACAO_EQUIPAMENTO";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CalibracaoEquipamento.class));
    }

    // GET calibração por ID
    @GetMapping("/{id}")
    public CalibracaoEquipamento getCalibracaoById(@PathVariable Integer id) {
        String sql = "SELECT * FROM TB_CALIBRACAO_EQUIPAMENTO WHERE CD_CALIBRACAO_EQUIPAMENTO = ?";

        return jdbcTemplate.queryForObject(
                sql,
                new BeanPropertyRowMapper<>(CalibracaoEquipamento.class),
                id
        );
    }

    // GET calibrações ativas
    @GetMapping("/ativas")
    public List<CalibracaoEquipamento> getCalibracoesAtivas() {
        String sql = "SELECT * FROM TB_CALIBRACAO_EQUIPAMENTO WHERE FG_SITUACAO = 'A'";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CalibracaoEquipamento.class));
    }

    // GET com filtro por equipamento
    @GetMapping("/buscar")
    public List<CalibracaoEquipamento> getCalibracoesByEquipamento(@RequestParam String equipamento) {
        String sql = "SELECT * FROM TB_CALIBRACAO_EQUIPAMENTO WHERE DS_EQUIPAMENTO LIKE ?";

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(CalibracaoEquipamento.class),
                "%" + equipamento + "%"
        );
    }

    // GET versão simples (apenas alguns campos)
    @GetMapping("/simples")
    public List<Map<String, Object>> getCalibracoesSimples() {
        String sql = "SELECT " +
                "CD_CALIBRACAO_EQUIPAMENTO, " +
                "NR_EQUIPAMENTO, " +
                "DS_EQUIPAMENTO, " +
                "DS_MARCA, " +
                "FG_SITUACAO " +
                "FROM TB_CALIBRACAO_EQUIPAMENTO";

        return jdbcTemplate.queryForList(sql);
    }

    // GET contagem total
    @GetMapping("/contagem")
    public Map<String, Object> getContagemCalibracoes() {
        String sql = "SELECT " +
                "COUNT(*) as total, " +
                "COUNT(CASE WHEN FG_SITUACAO = 'A' THEN 1 END) as ativas, " +
                "COUNT(CASE WHEN FG_SITUACAO = 'I' THEN 1 END) as inativas " +
                "FROM TB_CALIBRACAO_EQUIPAMENTO";

        return jdbcTemplate.queryForMap(sql);
    }
}