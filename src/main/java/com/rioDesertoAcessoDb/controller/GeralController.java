package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.service.GeralService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/geral")
@Tag(name = "Geral", description = "Endpoints para operações gerais do sistema")
public class GeralController {

        @Autowired
        private GeralService geralService;

        @Operation(summary = "Obter contadores do sistema", description = "Retorna os contadores de registros ativos do sistema Zeus e RDLab")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Contadores obtidos com sucesso", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Map.class), examples = @ExampleObject(name = "Contadores", value = "{\"contadoresZeus\": 150, \"contadoresRdLab\": 120}"))),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Map.class), examples = @ExampleObject(name = "Erro", value = "{\"timestamp\": \"2024-12-13T10:30:00\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Erro ao consultar contadores\"}")))
        })
        @GetMapping(value = "/contadores", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<Map<String, Long>> getContadores() {
                Map<String, Long> contadores = geralService.getContadores();
                return ResponseEntity.ok(contadores);
        }

        @Operation(summary = "Obter últimos movimentos RD Lab", description = "Retorna os últimos 10 movimentos do laboratório, incluindo dados da amostra, identificação e piezômetro")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Movimentos obtidos com sucesso", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = java.util.List.class))),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
        })
        @GetMapping(value = "/ultimos-movimentos-rd-lab", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<java.util.List<java.util.Map<String, Object>>> getUltimosMovimentos() {
                java.util.List<java.util.Map<String, Object>> movimentos = geralService.getUltimosMovimentos();
                return ResponseEntity.ok(movimentos);
        }

        @Operation(summary = "Obter últimos movimentos do Sistema", description = "Retorna os últimos 10 registros combinados de inspeção de piezômetro, nível de água e recursos hídricos")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Dados obtidos com sucesso", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = java.util.List.class))),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
        })
        @GetMapping(value = "/ultimos-movimentos-zeus", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<java.util.List<java.util.Map<String, Object>>> getUltimosMovimentosSistema() {
                java.util.List<java.util.Map<String, Object>> movimentos = geralService.getUltimosMovimentosSistema();
                return ResponseEntity.ok(movimentos);
        }
}