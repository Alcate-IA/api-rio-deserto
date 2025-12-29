package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.model.AvaliacaoAnaliseIaQualidadeAgua;
import com.rioDesertoAcessoDb.service.AvaliacaoAnaliseIaQualidadeAguaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/avaliacoes-analise-ia-qualidade-agua")
@Tag(name = "Avaliações IA - Qualidade da Água", description = "APIs para gerenciamento de avaliações de IA para análise de qualidade da água")
public class AvaliacaoAnaliseIaQualidadeAguaController {

    @Autowired
    private AvaliacaoAnaliseIaQualidadeAguaService service;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    @Operation(summary = "Listar todas as avaliações", description = "Retorna uma lista de todas as avaliações de análise de IA para qualidade da água registradas")
    @ApiResponse(responseCode = "200", description = "Lista de avaliações retornada com sucesso")
    public List<AvaliacaoAnaliseIaQualidadeAgua> getAll() {
        return service.findAll();
    }

    @GetMapping("/zeus/{idZeus}")
    @Operation(summary = "Listar avaliações por ID Zeus", description = "Retorna uma lista de avaliações de análise de IA para um registro do Zeus específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de avaliações retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Registro Zeus não encontrado ou sem avaliações")
    })
    public List<AvaliacaoAnaliseIaQualidadeAgua> getByIdZeus(@PathVariable Integer idZeus) {
        return service.findByIdZeus(idZeus);
    }

    @PostMapping
    @Operation(summary = "Criar nova avaliação", description = "Registra uma nova avaliação de análise de IA para a qualidade da água")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avaliação enviada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou dado inválido"),
            @ApiResponse(responseCode = "500", description = "Erro ao salvar a avaliação")
    })
    public ResponseEntity<Map<String, String>> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados da avaliação", required = true, content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = AvaliacaoAnaliseIaQualidadeAgua.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\n  \"idZeus\": 5,\n  \"editouAnalise\": true,\n  \"analiseOriginal\": \"exemplo analisel\",\n  \"analiseEditada\": \"exemplo analise editada\",\n  \"nota\": 5,\n  \"comentario\": \"comentario\"\n}"))) @RequestBody JsonNode node) {
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
            String[] numberFields = { "idZeus", "nota" };
            for (String field : numberFields) {
                if (node.has(field) && !node.get(field).isNumber() && !node.get(field).isNull()) {
                    return errorResponse("Erro de tipo",
                            "O campo '" + field + "' tentou entrar com outro tipo de dado");
                }
            }

            AvaliacaoAnaliseIaQualidadeAgua avaliacao = objectMapper.treeToValue(node,
                    AvaliacaoAnaliseIaQualidadeAgua.class);

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
                    "Não foi possível enviar sua avaliação no momento. Contate-nos se o erro persistir.");
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
