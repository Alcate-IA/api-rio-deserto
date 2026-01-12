package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.dtos.InspecaoPiezometroRequest;
import com.rioDesertoAcessoDb.dtos.NivelAguaRequest;
import com.rioDesertoAcessoDb.dtos.RecursoHidricoRequest;
import com.rioDesertoAcessoDb.model.InspecaoPiezometroMovimento;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inspecoes-piezometros-movimentos")
@Tag(name = "Inspeção de Piezômetros", description = "APIs para gerenciamento de inspeções de piezômetros")
public class InspecaoPiezometroMovimentoController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    @PostMapping("/inserir-leitura-pp-pb")
    @Operation(summary = "Inserir inspeção de piezômetro do tipo PP e PB",
            description = "Executa a procedure SP_INSERE_INSPECAO_PZ no banco Firebird Zeus para registrar uma nova inspeção de piezômetro. "
                    + "Campos obrigatórios: cdPiezometro, dtInspecao, qtLeitura, qtNivelEstatico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inspeção inserida com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro ao executar a procedure no banco de dados",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Map<String, Object>> inserirInspecaoPpPb(
            @Valid @RequestBody InspecaoPiezometroRequest request) {

        Map<String, Object> response = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(FIREBIRD_URL, FIREBIRD_USER, FIREBIRD_PASSWORD);
             CallableStatement stmt = conn.prepareCall("{call SP_INSERE_INSPECAO_PZ(?, ?, ?, ?, ?)}")) {

            stmt.setInt(1, request.getCdPiezometro());

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
                response.put("status", "erro");
                response.put("mensagem", "Data inválida: " + dateStr + ". Use dd.MM.yyyy, dd/MM/yyyy ou yyyy-MM-dd");
                return ResponseEntity.badRequest().body(response);
            }

            stmt.setDate(2, sqlDate);
            stmt.setDouble(3, request.getQtLeitura());
            stmt.setDouble(4, request.getQtNivelEstatico());

            if (request.getObservacao() != null && !request.getObservacao().trim().isEmpty()) {
                stmt.setString(5, request.getObservacao());
            } else {
                stmt.setNull(5, java.sql.Types.VARCHAR);
            }

            conn.setAutoCommit(false);
            stmt.execute();
            conn.commit();

            response.put("status", "sucesso");
            response.put("mensagem", "Inspeção PP/PB inserida com sucesso");
            response.put("cdPiezometro", request.getCdPiezometro());
            response.put("dtInspecao", request.getDtInspecao());
            response.put("qtLeitura", request.getQtLeitura());
            response.put("qtNivelEstatico", request.getQtNivelEstatico());
            if (request.getObservacao() != null) {
                response.put("observacao", request.getObservacao());
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();

            response.put("status", "erro");
            response.put("mensagem", "Erro ao inserir inspeção PP/PB: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/inserir-nivel-agua-pr")
    @Operation(summary = "Inserir nível de água do tipo PR",
            description = "Executa a procedure SP_INSERE_NIVEL_AGUA_PZ no banco Firebird Zeus para registrar um novo nível de água de piezômetro do tipo PR. "
                    + "Campos obrigatórios: cdPiezometro, dtInspecao, qtNivelEstatico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nível de água PR inserido com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro ao executar a procedure no banco de dados",
                    content = @Content(mediaType = "application/json"))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados do nível de água para piezômetros PR",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = NivelAguaRequest.class),
                    examples = @ExampleObject(
                            name = "Exemplo PR",
                            summary = "Exemplo de nível de água para PR",
                            description = "Exemplo de requisição para piezômetros do tipo PR",
                            value = """
                            {
                              "cdPiezometro": 685,
                              "dtInspecao": "11.12.2025",
                              "qtNivelEstatico": 30,
                              "observacao": "AAAAAAAAAAA"
                            }
                            """
                    )
            )
    )
    public ResponseEntity<Map<String, Object>> inserirNivelAguaPR(
            @Valid @RequestBody NivelAguaRequest request) {

        Map<String, Object> response = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(FIREBIRD_URL, FIREBIRD_USER, FIREBIRD_PASSWORD);
             CallableStatement stmt = conn.prepareCall("{call SP_INSERE_NIVEL_AGUA_PZ(?, ?, ?, ?)}")) {

            stmt.setInt(1, request.getCdPiezometro());

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
                response.put("status", "erro");
                response.put("mensagem", "Data inválida: " + dateStr + ". Use dd.MM.yyyy, dd/MM/yyyy ou yyyy-MM-dd");
                return ResponseEntity.badRequest().body(response);
            }

            stmt.setDate(2, sqlDate);
            stmt.setDouble(3, request.getQtNivelEstatico());

            if (request.getObservacao() != null && !request.getObservacao().trim().isEmpty()) {
                stmt.setString(4, request.getObservacao());
            } else {
                stmt.setNull(4, java.sql.Types.VARCHAR);
            }

            conn.setAutoCommit(false);
            stmt.execute();
            conn.commit();

            response.put("status", "sucesso");
            response.put("mensagem", "Nível de água PR inserido com sucesso");
            response.put("cdPiezometro", request.getCdPiezometro());
            response.put("dtInspecao", request.getDtInspecao());
            response.put("qtNivelEstatico", request.getQtNivelEstatico());
            if (request.getObservacao() != null) {
                response.put("observacao", request.getObservacao());
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();

            response.put("status", "erro");
            response.put("mensagem", "Erro ao inserir nível de água PR: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/inserir-recurso-hidrico-pc-pv")
    @Operation(summary = "Inserir recurso hídrico dos tipos PC e PV",
            description = "Executa a procedure SP_INSERE_RECURSO_HIDRICO_PZ no banco Firebird Zeus para registrar um novo recurso hídrico de piezômetro dos tipos PC e PV. "
                    + "Campos obrigatórios: cdPiezometro, dtInspecao, qtVazao")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recurso hídrico PC/PV inserido com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro ao executar a procedure no banco de dados",
                    content = @Content(mediaType = "application/json"))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados do recurso hídrico para piezômetros PC e PV",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RecursoHidricoRequest.class),
                    examples = @ExampleObject(
                            name = "Exemplo PC/PV",
                            summary = "Exemplo de recurso hídrico para PC/PV",
                            description = "Exemplo de requisição para piezômetros dos tipos PC e PV",
                            value = """
                            {
                              "cdPiezometro": 687,
                              "dtInspecao": "11.12.2025",
                              "qtVazao": 33,
                              "observacao": "AAAAAAAAAAA"
                            }
                            """
                    )
            )
    )

    public ResponseEntity<Map<String, Object>> inserirRecursoHidricoPCPV(
            @Valid @RequestBody RecursoHidricoRequest request) {

        Map<String, Object> response = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(FIREBIRD_URL, FIREBIRD_USER, FIREBIRD_PASSWORD);
             CallableStatement stmt = conn.prepareCall("{call SP_INSERE_RECURSO_HIDRICO_PZ(?, ?, ?, ?)}")) {

            stmt.setInt(1, request.getCdPiezometro());

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
                response.put("status", "erro");
                response.put("mensagem", "Data inválida: " + dateStr + ". Use dd.MM.yyyy, dd/MM/yyyy ou yyyy-MM-dd");
                return ResponseEntity.badRequest().body(response);
            }

            stmt.setDate(2, sqlDate);
            stmt.setDouble(3, request.getQtVazao());

            if (request.getObservacao() != null && !request.getObservacao().trim().isEmpty()) {
                stmt.setString(4, request.getObservacao());
            } else {
                stmt.setNull(4, java.sql.Types.VARCHAR);
            }

            conn.setAutoCommit(false);
            stmt.execute();
            conn.commit();

            response.put("status", "sucesso");
            response.put("mensagem", "Recurso hídrico PC/PV inserido com sucesso");
            response.put("cdPiezometro", request.getCdPiezometro());
            response.put("dtInspecao", request.getDtInspecao());
            response.put("qtVazao", request.getQtVazao());
            if (request.getObservacao() != null) {
                response.put("observacao", request.getObservacao());
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();

            response.put("status", "erro");
            response.put("mensagem", "Erro ao inserir recurso hídrico PC/PV: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}