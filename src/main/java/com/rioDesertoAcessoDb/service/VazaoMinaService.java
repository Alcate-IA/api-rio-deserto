package com.rioDesertoAcessoDb.service;

import com.rioDesertoAcessoDb.repositories.VazaoMinaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class VazaoMinaService {

    private final VazaoMinaRepository vazaoMinaRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public VazaoMinaService(VazaoMinaRepository vazaoMinaRepository) {
        this.vazaoMinaRepository = vazaoMinaRepository;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public Map<String, Object> getVazaoData() {
        Map<String, Object> response = new HashMap<>();

        // Obter estatísticas da primeira query
        Map<String, Object> estatisticas = vazaoMinaRepository.findEstatisticasVazao();

        if (estatisticas != null && !estatisticas.isEmpty()) {
            response.put("resultado", estatisticas);
        } else {
            response.put("resultado", new HashMap<>());
        }

        // Obter histórico completo da segunda query
        List<Object[]> historico = vazaoMinaRepository.findHistoricoCompleto();
        List<Map<String, Object>> historicoFormatado = historico.stream()
                .map(registro -> {
                    Map<String, Object> registroMap = new HashMap<>();
                    registroMap.put("vazao_bombeamento", registro[0]);
                    registroMap.put("mes_ano_vazao", registro[1].toString()); // Converter para String
                    registroMap.put("id", registro[2]);
                    return registroMap;
                })
                .toList();

        response.put("historicoCompleto", historicoFormatado);

        // Chamar o webhook com o histórico completo
        Map<String, Object> analiseIa = chamarWebhookAnalise(historicoFormatado);
        response.put("analiseIa", analiseIa);

        return response;
    }

    private Map<String, Object> chamarWebhookAnalise(List<Map<String, Object>> historicoCompleto) {
        try {
            String webhookUrl = "http://192.168.100.95:5678/webhook/analise_mina_total";

            // Versão SIMPLES igual ao endpoint de teste
            String response = restTemplate.postForObject(
                    webhookUrl,
                    Map.of("historico", historicoCompleto), // Usar Map.of como no teste
                    String.class
            );

            // Tentar parsear a resposta como JSON
            try {
                return objectMapper.readValue(response, Map.class);
            } catch (Exception e) {
                // Se não for JSON válido, retornar como string
                return Map.of(
                        "resposta_raw", response,
                        "aviso", "Resposta não é JSON válido"
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
            return criarRespostaErro("Exceção ao chamar webhook: " + e.getMessage());
        }
    }

    private Map<String, Object> criarRespostaErro(String mensagem) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("erro", mensagem);
        erro.put("timestamp", new Date());
        return erro;
    }
}