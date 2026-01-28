package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.dtos.VazaoMinaRequest;
import com.rioDesertoAcessoDb.model.VazaoMina;
import com.rioDesertoAcessoDb.service.VazaoMinaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "Inserir registro de vazão da mina", description = "Cria um novo registro com mês, ano e vazão de bombeamento. O id é gerado pelo banco.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VazaoMina> criar(@Valid @RequestBody VazaoMinaRequest request) {
        VazaoMina criado = vazaoMinaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }
}
