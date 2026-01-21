package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.model.AvaliacaoAnaliseIaNivelEstatico;
import com.rioDesertoAcessoDb.service.AvaliacaoAnaliseIaNivelEstaticoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.client.RestTemplate;

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

    @PutMapping("/{idAvaliacao}/marcar-como-analisada")
    @Operation(summary = "Marcar avaliação como analisada pela IA", description = "Atualiza o campo ia_analisou para true para uma avaliação específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avaliação marcada como analisada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Avaliação já está marcada como analisada"),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada")
    })
    public ResponseEntity<?> marcarComoAnalisada(@PathVariable Integer idAvaliacao) {
        return service.findById(idAvaliacao).map(avaliacao -> {
            Map<String, String> response = new HashMap<>();
            if (Boolean.TRUE.equals(avaliacao.getIaAnalisou())) {
                response.put("message", "Essa análise (id: " + idAvaliacao + ") já está marcada como analisada");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            avaliacao.setIaAnalisou(true);
            service.save(avaliacao);
            response.put("message", "a análise (id: " + idAvaliacao + ") foi marcada como analisada");
            return ResponseEntity.ok(response);
        }).orElseGet(() -> {
            Map<String, String> response = new HashMap<>();
            response.put("message", "a análise não foi encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        });
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

            AvaliacaoAnaliseIaNivelEstatico avaliacaoSalva = service.save(avaliacao);

            // WEBHOOK SIMPLES - igual à primeira função
            String webhookUrl = "http://192.168.100.95:5678/webhook/recebe_analises_rag";

            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(90000);
            factory.setReadTimeout(90000);

            RestTemplate restTemplate = new RestTemplate(factory);

            try {
                ResponseEntity<Object> webhookResponse = restTemplate.postForEntity(
                        webhookUrl, avaliacaoSalva, Object.class);

            } catch (Exception e) {
                System.out.println("Erro ao enviar webhook: " + e.getMessage());
                e.printStackTrace();
            }

            response.put("title", "Avaliação enviada!");
            response.put("text", "Obrigado pelo seu feedback.");
            return ResponseEntity.ok(response);

        } catch (DataIntegrityViolationException e) {
            System.out.println("Erro de integridade de dados: " + e.getMessage());
            return errorResponse("Erro ao salvar", "esse piezometro não existe");
        } catch (Exception e) {
            System.out.println("Erro geral ao salvar avaliação: " + e.getMessage());
            e.printStackTrace();
            return errorResponse("Erro ao salvar",
                    "Não foi possível enviar sua avaliação no momento. Contate-nos se o erro permitir");
        }
    }

    // Método auxiliar para respostas de erro
    private ResponseEntity<Map<String, String>> errorResponse(String title, String text) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("title", title);
        errorResponse.put("text", text);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(HttpMessageNotReadableException ex) {
        return errorResponse("Erro de tipo", "tentou entrar com outro tipo de dado");
    }


    @PostMapping("/analisar")
    @Operation(summary = "Solicitar análise de IA", description = "Envia os dados para o webhook de IA e retorna a análise gerada")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Análise retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao comunicar com o webhook de IA")
    })
    public ResponseEntity<?> solicitarAnaliseIa(@RequestBody Map<String, Object> payload) {
        String webhookUrl = "http://192.168.100.95:5678/webhook/envio-analise-db";

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(90000);
        factory.setReadTimeout(90000);

        RestTemplate restTemplate = new RestTemplate(factory);

        try {
            ResponseEntity<Object> response = restTemplate.postForEntity(webhookUrl, payload, Object.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erro ao comunicar com o webhook: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
