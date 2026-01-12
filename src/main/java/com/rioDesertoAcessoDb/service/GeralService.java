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

        Long contadoresZeus = geralRepository.getContadoresZeus();
        Long contadoresRdLab = geralRepository.getContadoresRdLab();

        contadores.put("contadoresZeus", contadoresZeus);
        contadores.put("contadoresRdLab", contadoresRdLab);

        return contadores;
    }

    public java.util.List<java.util.Map<String, Object>> getUltimosMovimentos() {
        return geralRepository.getUltimosMovimentos();
    }

    public java.util.List<java.util.Map<String, Object>> getUltimosMovimentosSistema() {
        return geralRepository.getUltimosMovimentosSistema();
    }
}