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

        Map<String, Object> estatisticas = vazaoMinaRepository.findEstatisticasVazao();

        if (estatisticas != null && !estatisticas.isEmpty()) {
            response.put("resultado", estatisticas);
        } else {
            response.put("resultado", new HashMap<>());
        }

        Map<String, Object> analisesSazonais = vazaoMinaRepository.findTendenciaSazonalVazao();

        if (analisesSazonais != null && !analisesSazonais.isEmpty()) {
            Map<String, Object> analisesSazonaisMutavel = new HashMap<>(analisesSazonais);

            List<Map<String, Object>> historicoList = new ArrayList<>();

            if (analisesSazonaisMutavel.get("historico_variacoes") instanceof String) {
                try {
                    String historicoJson = (String) analisesSazonaisMutavel.get("historico_variacoes");
                    historicoList = objectMapper.readValue(
                            historicoJson,
                            objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class)
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (analisesSazonaisMutavel.get("historico_variacoes") instanceof List) {
                historicoList = (List<Map<String, Object>>) analisesSazonaisMutavel.get("historico_variacoes");
            }

            Map<String, Object> dadosAnalise = calcularEstatisticasTendencia(historicoList);

            dadosAnalise.put("historico", historicoList);

            analisesSazonaisMutavel.remove("historico_variacoes");
            analisesSazonaisMutavel.put("dados", dadosAnalise);

            response.put("analisesSazonais", analisesSazonaisMutavel);
        } else {
            response.put("analisesSazonais", new HashMap<>());
        }

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

        Map<String, Object> analiseIa = chamarWebhookAnalise(historicoFormatado);
        response.put("analiseIa", analiseIa);

        return response;
    }

    /**
     * Calcula estatísticas de tendência a partir da lista histórica
     */
    private Map<String, Object> calcularEstatisticasTendencia(List<Map<String, Object>> historicoList) {
        Map<String, Object> estatisticas = new HashMap<>();

        if (historicoList == null || historicoList.isEmpty()) {
            return estatisticas;
        }

        // Inicializar variáveis
        Map<String, Object> maiorTendencia = null;
        Map<String, Object> menorTendencia = null;
        int positivas = 0;
        int negativas = 0;

        for (Map<String, Object> item : historicoList) {
            try {
                // Converter porcentagem para double
                Object variacaoObj = item.get("variacao_percentual");
                if (variacaoObj == null) continue;

                double variacao = 0.0;
                if (variacaoObj instanceof Number) {
                    variacao = ((Number) variacaoObj).doubleValue();
                } else if (variacaoObj instanceof String) {
                    variacao = Double.parseDouble((String) variacaoObj);
                }

                // Verificar se é maior tendência
                if (maiorTendencia == null ||
                        ((Number) maiorTendencia.get("porcentagem")).doubleValue() < variacao) {
                    maiorTendencia = new HashMap<>();
                    maiorTendencia.put("ano", item.get("ano"));
                    maiorTendencia.put("porcentagem", variacao);
                }

                // Verificar se é menor tendência
                if (menorTendencia == null ||
                        ((Number) menorTendencia.get("porcentagem")).doubleValue() > variacao) {
                    menorTendencia = new HashMap<>();
                    menorTendencia.put("ano", item.get("ano"));
                    menorTendencia.put("porcentagem", variacao);
                }

                // Contar positivas e negativas
                if (variacao > 0) {
                    positivas++;
                } else if (variacao < 0) {
                    negativas++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Adicionar às estatísticas
        if (maiorTendencia != null) {
            estatisticas.put("maiorTendencia", maiorTendencia);
        }

        if (menorTendencia != null) {
            estatisticas.put("menorTendencia", menorTendencia);
        }

        // Determinar tendência geral
        boolean tendenciaPositiva = positivas > negativas;
        estatisticas.put("tendencia", tendenciaPositiva);
        estatisticas.put("contagem", Map.of(
                "positivas", positivas,
                "negativas", negativas,
                "total", historicoList.size()
        ));

        return estatisticas;
    }

    private Map<String, Object> chamarWebhookAnalise(List<Map<String, Object>> historicoCompleto) {
        try {
            String webhookUrl = "http://192.168.100.95:5678/webhook/analise_mina_total";

            String response = restTemplate.postForObject(
                    webhookUrl,
                    Map.of("historico", historicoCompleto),
                    String.class
            );

            try {
                return objectMapper.readValue(response, Map.class);
            } catch (Exception e) {
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