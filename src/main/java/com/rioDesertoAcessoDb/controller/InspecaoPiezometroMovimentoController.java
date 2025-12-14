package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.dtos.InspecaoPiezometroRequest;
import com.rioDesertoAcessoDb.model.InspecaoPiezometroMovimento;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inspecoes-piezometros-movimentos")
@Tag(name = "Inspeção de Piezômetros", description = "APIs para gerenciamento de inspeções de piezômetros")
public class InspecaoPiezometroMovimentoController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Configurações de conexão Firebird Zeus
    private static final String FIREBIRD_URL = "jdbc:firebirdsql://192.9.200.7:3050//data1/dataib/zeus20.fdb?encoding=WIN1252";
    private static final String FIREBIRD_USER = "ALCATEIA";
    private static final String FIREBIRD_PASSWORD = "8D5Z9s2F";

    @GetMapping
    @Operation(summary = "Listar inspeções de piezômetros", description = "Retorna as primeiras 100 inspeções de piezômetros")
    @ApiResponse(responseCode = "200", description = "Lista de inspeções retornada com sucesso")
    public List<InspecaoPiezometroMovimento> getAllInspecoesMovimentos() {
        String sql = "SELECT FIRST 100 * FROM TB_INSPECAO_PIEZOMETRO_MVTO";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(InspecaoPiezometroMovimento.class));
    }

    @PostMapping
    @Operation(summary = "Inserir inspeção de piezômetro", description = "Executa a procedure SP_INSERE_INSPECAO_PZ no banco Firebird Zeus para registrar uma nova inspeção de piezômetro. "
            +
            "Esta API é utilizada pelo app de campo e pela automação para inserir dados de inspeção.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inspeção inserida com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro ao executar a procedure no banco de dados", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Map<String, String>> inserirInspecao(
            @Valid @RequestBody InspecaoPiezometroRequest request) {

        Map<String, String> response = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(FIREBIRD_URL, FIREBIRD_USER, FIREBIRD_PASSWORD);
                CallableStatement stmt = conn.prepareCall("{call SP_INSERE_INSPECAO_PZ(?, ?, ?, ?)}")) {

            // Configurar parâmetros da procedure
            stmt.setInt(1, request.getCdPiezometro());

            // Tratamento de data (dd.MM.yyyy ou dd/MM/yyyy -> java.sql.Date)
            String dateStr = request.getDtInspecao();
            java.sql.Date sqlDate;
            try {
                java.text.SimpleDateFormat sdf;
                if (dateStr.contains(".")) {
                    sdf = new java.text.SimpleDateFormat("dd.MM.yyyy");
                } else if (dateStr.contains("/")) {
                    sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                } else {
                    sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                }
                java.util.Date date = sdf.parse(dateStr);
                sqlDate = new java.sql.Date(date.getTime());
            } catch (Exception e) {
                throw new IllegalArgumentException("Data inválida: " + dateStr + ". Use dd.MM.yyyy ou dd/MM/yyyy");
            }
            stmt.setDate(2, sqlDate);
            stmt.setDouble(3, request.getQtLeitura());
            stmt.setDouble(4, request.getQtNivelEstatico());

            // Executar a procedure
            System.out.println("Executando procedure SP_INSERE_INSPECAO_PZ...");

            // Garantir que a transação seja commitada
            conn.setAutoCommit(false);
            stmt.execute();
            conn.commit();

            System.out.println("Procedure executada com sucesso e commit realizado!");

            response.put("status", "sucesso");
            response.put("mensagem", "Inspeção inserida com sucesso");
            response.put("cdPiezometro", request.getCdPiezometro().toString());
            response.put("dtInspecao", request.getDtInspecao());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace(); // Logar no console

            response.put("status", "erro");
            response.put("mensagem", "Erro ao inserir inspeção: " + e.getMessage());

            // Capturar stack trace
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            response.put("detalhes", sw.toString());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}