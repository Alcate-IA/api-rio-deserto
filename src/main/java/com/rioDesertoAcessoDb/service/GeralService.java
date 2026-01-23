package com.rioDesertoAcessoDb.service;

import com.rioDesertoAcessoDb.repositories.GeralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GeralService {

    @Autowired
    private GeralRepository geralRepository;

    public Map<String, Long> getContadores() {
        Map<String, Long> contadores = new HashMap<>();

        Long contadoresZeus = geralRepository.getContadoresZeusTotais();
        Long contadoresRdLab = geralRepository.getContadoresRdLabTotais();

        Long contadoresZeusAtivos = geralRepository.getContadoresZeusAtivos();
        Long contadoresRdLabAtivos = geralRepository.getContadoresRdLabAtivos();

        Long contadoresZeusInativos = geralRepository.getContadoresZeusInativos();
        Long contadoresRdLabInativos = geralRepository.getContadoresRdLabInativos();

        contadores.put("contadoresZeus", contadoresZeus);
        contadores.put("contadoresRdLab", contadoresRdLab);

        contadores.put("contadoresZeusAtivos", contadoresZeusAtivos);
        contadores.put("contadoresRdLabAtivos", contadoresRdLabAtivos);

        contadores.put("contadoresZeusInativos", contadoresZeusInativos);
        contadores.put("contadoresRdLabInativos", contadoresRdLabInativos);

        return contadores;
    }

    public java.util.List<java.util.Map<String, Object>> getUltimosMovimentos() {
        return geralRepository.getUltimosMovimentos();
    }

    public java.util.List<java.util.Map<String, Object>> getUltimosMovimentosSistema() {
        java.util.List<java.util.Map<String, Object>> movimentos = geralRepository.getUltimosMovimentosSistema();

        for (java.util.Map<String, Object> movimento : movimentos) {
            try {
                Integer cdPiezometro = (Integer) movimento.get("cd_piezometro");
                String origem = (String) movimento.get("origem");
                Object valorObj = movimento.get("nivel_estatico");
                if (valorObj == null) {
                    valorObj = movimento.get("vazao");
                }

                if (cdPiezometro != null && origem != null && valorObj != null) {
                    java.util.Map<String, Object> estatisticas = geralRepository.getEstatisticasPiezometro(cdPiezometro,
                            origem);

                    if (estatisticas != null && !estatisticas.isEmpty()) {
                        java.math.BigDecimal valor = null;
                        if (valorObj instanceof java.math.BigDecimal) {
                            valor = (java.math.BigDecimal) valorObj;
                        } else if (valorObj instanceof Number) {
                            valor = new java.math.BigDecimal(((Number) valorObj).doubleValue());
                        }

                        java.math.BigDecimal media = (java.math.BigDecimal) estatisticas.get("media");
                        java.math.BigDecimal maiorLeitura = (java.math.BigDecimal) estatisticas.get("maior_leitura");
                        java.math.BigDecimal menorLeitura = (java.math.BigDecimal) estatisticas.get("menor_leitura");

                        java.util.Map<String, Boolean> dados = new HashMap<>();
                        if (valor != null) {
                            dados.put("media", media != null && valor.compareTo(media) > 0);
                            dados.put("maior_leitura", maiorLeitura != null && valor.compareTo(maiorLeitura) == 0);
                            dados.put("menor_leitura", menorLeitura != null && valor.compareTo(menorLeitura) == 0);
                        } else {
                            dados.put("media", false);
                            dados.put("maior_leitura", false);
                            dados.put("menor_leitura", false);
                        }
                        movimento.put("dados", dados);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return movimentos;
    }

    public java.util.List<java.util.Map<String, Object>> getDadosVazaoXPrecipitacao() {
        return geralRepository.getDadosVazaoXPrecipitacao();
    }
}