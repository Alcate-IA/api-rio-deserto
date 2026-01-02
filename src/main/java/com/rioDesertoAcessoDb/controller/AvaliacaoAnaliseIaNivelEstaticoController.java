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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/avaliacoes-analise-ia-nivel-estatico")
@Tag(name = "Avaliações IA - Nível Estático", description = "APIs para gerenciamento de avaliações de IA para nível estático de piezômetros")
public class AvaliacaoAnaliseIaNivelEstaticoController {

    @Autowired
    private AvaliacaoAnaliseIaNivelEstaticoService service;

    @Autowired
    private ObjectMapper objectMapper;

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

    @GetMapping("/nao-analisadas-ia")
    @Operation(summary = "Listar avaliações não analisadas pela IA", description = "Retorna uma lista de todas as avaliações onde a IA ainda não realizou a análise")
    @ApiResponse(responseCode = "200", description = "Lista de avaliações não analisadas retornada com sucesso")
    public List<AvaliacaoAnaliseIaNivelEstatico> getNaoAnalisadas() {
        return service.findNaoAnalisadas();
    }

    @PostMapping
    @Operation(summary = "Criar nova avaliação", description = "Registra uma nova avaliação de análise de IA para o nível estático")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avaliação enviada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou dado inválido"),
            @ApiResponse(responseCode = "500", description = "Erro ao salvar a avaliação")
    })
    public ResponseEntity<Map<String, String>> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados da avaliação", required = true, content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = AvaliacaoAnaliseIaNivelEstatico.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\n  \"cdPiezometro\": 206,\n  \"editouAnalise\": true,\n  \"analiseOriginal\": \"exemplo analisel\",\n  \"analiseEditada\": \"exemplo analise editada\",\n  \"nota\": 5,\n  \"comentario\": \"comentario\"\n}"))) @RequestBody JsonNode node) {
        Map<String, String> response = new HashMap<>();

        try {
            // Strict Validation for Boolean
            if (node.has("editouAnalise") && !node.get("editouAnalise").isBoolean()) {
                return errorResponse("Erro de tipo", "O campo 'editouAnalise' tentou entrar com outro tipo de dado");
            }

            // Strict Validation for Strings
            String[] stringFields = { "analiseOriginal", "analiseEditada", "comentario" };
            for (String field : stringFields) {
                if (node.has(field) && !node.get(field).isTextual() && !node.get(field).isNull()) {
                    return errorResponse("Erro de tipo",
                            "O campo '" + field + "' tentou entrar com outro tipo de dado");
                }
            }

            // Strict Validation for Numbers
            String[] numberFields = { "cdPiezometro", "nota" };
            for (String field : numberFields) {
                if (node.has(field) && !node.get(field).isNumber() && !node.get(field).isNull()) {
                    return errorResponse("Erro de tipo",
                            "O campo '" + field + "' tentou entrar com outro tipo de dado");
                }
            }

            AvaliacaoAnaliseIaNivelEstatico avaliacao = objectMapper.treeToValue(node,
                    AvaliacaoAnaliseIaNivelEstatico.class);

            if (avaliacao.getNota() != null && (avaliacao.getNota() < 1 || avaliacao.getNota() > 10)) {
                return errorResponse("Erro de validação", "A nota deve ser um número de 1 a 10");
            }

            service.save(avaliacao);
            response.put("title", "Avaliação enviada!");
            response.put("text", "Obrigado pelo seu feedback.");
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            return errorResponse("Erro ao salvar", "esse piezometro não existe");
        } catch (Exception e) {
            return errorResponse("Erro ao salvar",
                    "Não foi possível enviar sua avaliação no momento. Contate-nos se o erro permitir");
        }
    }

    private ResponseEntity<Map<String, String>> errorResponse(String title, String text) {
        Map<String, String> response = new HashMap<>();
        response.put("title", title);
        response.put("text", text);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(HttpMessageNotReadableException ex) {
        return errorResponse("Erro de tipo", "tentou entrar com outro tipo de dado");
    }
}
