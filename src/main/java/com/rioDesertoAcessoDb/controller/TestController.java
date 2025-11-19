package com.rioDesertoAcessoDb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/test")
    public String testConnection() {
        try {
            String result = jdbcTemplate.queryForObject(
                    "SELECT '✅ Conexão Firebird 2.5 OK!' FROM RDB$DATABASE",
                    String.class
            );
            return result;
        } catch (Exception e) {
            return "❌ Erro: " + e.getMessage();
        }
    }

    @GetMapping("/tables")
    public List<Map<String, Object>> listTables() {
        String sql = "SELECT RDB$RELATION_NAME as table_name " +
                "FROM RDB$RELATIONS " +
                "WHERE RDB$SYSTEM_FLAG = 0 " +
                "ORDER BY RDB$RELATION_NAME";

        return jdbcTemplate.queryForList(sql);
    }

    @GetMapping("/info")
    public Map<String, Object> getDatabaseInfo() {
        String sql = "SELECT " +
                "MON$DATABASE_NAME as db_name, " +
                "MON$PAGE_SIZE as page_size, " +
                "MON$SQL_DIALECT as sql_dialect " +
                "FROM MON$DATABASE";

        return jdbcTemplate.queryForMap(sql);
    }
}