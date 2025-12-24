package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.model.Analise;
import com.rioDesertoAcessoDb.repositories.AnaliseRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analises")
@Tag(name = "Análises", description = "APIs para consulta de tipos de análises")
public class AnaliseController {

    @Autowired
    private AnaliseRepository analiseRepository;

    @GetMapping
    @Operation(summary = "Listar todas as análises", description = "Retorna uma lista de todos os tipos de análises cadastrados sem nenhum filtro")
    public List<Analise> getAllAnalises() {
        return analiseRepository.findAll();
    }

    @GetMapping("/busca-por-nome/{nome}")
    @Operation(summary = "Buscar análises por nome", description = "Retorna uma lista de análises que contenham o texto informado no nome, ordenadas alfabeticamente")
    public List<Analise> getAnalisesByNome(@PathVariable String nome) {
        return analiseRepository.findByNomeContainingIgnoreCaseOrderByNomeAsc(nome);
    }

    @GetMapping("/busca-por-simbolo/{simbolo}")
    @Operation(summary = "Buscar análises por símbolo", description = "Retorna uma lista de análises que contenham o texto informado no símbolo, ordenadas alfabeticamente por nome")
    public List<Analise> getAnalisesBySimbolo(@PathVariable String simbolo) {
        return analiseRepository.findBySimboloContainingIgnoreCaseOrderByNomeAsc(simbolo);
    }
}
