package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.model.AvaliacaoAnaliseIaNivelEstatico;
import com.rioDesertoAcessoDb.service.AvaliacaoAnaliseIaNivelEstaticoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/avaliacoes-analise-ia-nivel-estatico")
@Tag(name = "Avaliações IA - Nível Estático", description = "APIs para gerenciamento de avaliações de IA para nível estático de piezômetros")
public class AvaliacaoAnaliseIaNivelEstaticoController {

    @Autowired
    private AvaliacaoAnaliseIaNivelEstaticoService service;

    @GetMapping
    @Operation(summary = "Listar todas as avaliações", description = "Retorna uma lista de todas as avaliações de análise de IA para nível estático registradas")
    @ApiResponse(responseCode = "200", description = "Lista de avaliações retornada com sucesso")
    public List<AvaliacaoAnaliseIaNivelEstatico> getAll() {
        return service.findAll();
    }

    @GetMapping("/piezometro/{cdPiezometro}")
    @Operation(summary = "Listar avaliações por piezômetro", description = "Retorna uma lista de avaliações de análise de IA para um piezômetro específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de avaliações retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Piezômetro não encontrado ou sem avaliações")
    })
    public List<AvaliacaoAnaliseIaNivelEstatico> getByIdPiezometro(@PathVariable Integer cdPiezometro) {
        return service.findByCdPiezometro(cdPiezometro);
    }

    @PostMapping
    @Operation(summary = "Criar nova avaliação", description = "Registra uma nova avaliação de análise de IA para o nível estático")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avaliação enviada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao salvar a avaliação")
    })
    public ResponseEntity<Map<String, String>> create(@RequestBody AvaliacaoAnaliseIaNivelEstatico avaliacao) {
        Map<String, String> response = new HashMap<>();
        try {
            service.save(avaliacao);
            response.put("title", "Avaliação enviada!");
            response.put("text", "Obrigado pelo seu feedback.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("title", "Erro ao salvar");
            response.put("text", "Não foi possível enviar sua avaliação no momento. Contate-nos se o erro permitir");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
