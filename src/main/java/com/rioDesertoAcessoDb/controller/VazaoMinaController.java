package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.service.VazaoMinaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/vazao-mina")
@Tag(name = "Vazão da Mina", description = "Endpoints vazão da mina")
public class VazaoMinaController {

    private final VazaoMinaService vazaoMinaService;

    public VazaoMinaController(VazaoMinaService vazaoMinaService) {
        this.vazaoMinaService = vazaoMinaService;
    }

    @Operation(summary = "Coleta e calcula dados do banco para mostrar na dashboard", description = "Retornar vários dados especificos para a dashboard, painel geral")
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> getEstatisticasVazao() {
        Map<String, Object> dados = vazaoMinaService.getVazaoData();
        return ResponseEntity.ok(dados);
    }
}
