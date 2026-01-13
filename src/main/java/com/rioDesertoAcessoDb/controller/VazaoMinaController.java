package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.service.VazaoMinaService;
import org.springframework.http.HttpStatus;
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
public class VazaoMinaController {

    private final VazaoMinaService vazaoMinaService;

    public VazaoMinaController(VazaoMinaService vazaoMinaService) {
        this.vazaoMinaService = vazaoMinaService;
    }

    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> getEstatisticasVazao() {
        Map<String, Object> dados = vazaoMinaService.getVazaoData();
        return ResponseEntity.ok(dados);
    }

    @GetMapping("/webhook")
    public ResponseEntity<?> testWebhook() {
        RestTemplate restTemplate = new RestTemplate();

        try {
            String url = "http://192.168.100.95:5678/webhook/analise_mina_total";

            Map<String, String> testRequest = new HashMap<>();
            testRequest.put("test", "ping");

            String response = restTemplate.postForObject(url, testRequest, String.class);

            return ResponseEntity.ok(Map.of(
                    "status", "sucesso",
                    "resposta", response,
                    "url", url
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "erro",
                            "mensagem", e.getMessage(),
                            "stacktrace", Arrays.toString(e.getStackTrace())
                    ));
        }
    }
}
