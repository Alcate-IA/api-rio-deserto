package com.rioDesertoAcessoDb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> executeQuery(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    public int executeUpdate(String sql) {
        return jdbcTemplate.update(sql);
    }

    public List<String> getTableNames() {
        String sql = "SELECT RDB$RELATION_NAME as table_name " +
                "FROM RDB$RELATIONS " +
                "WHERE RDB$SYSTEM_FLAG = 0 " +
                "ORDER BY RDB$RELATION_NAME";

        return jdbcTemplate.queryForList(sql, String.class);
    }

    public List<Map<String, Object>> getTableData(String tableName) {
        String sql = "SELECT FIRST 10 * FROM " + tableName;
        return jdbcTemplate.queryForList(sql);
    }
}