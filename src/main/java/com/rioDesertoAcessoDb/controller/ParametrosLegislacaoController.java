package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.model.ParametrosLegislacao;
import com.rioDesertoAcessoDb.repositories.ParametrosLegislacaoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/parametros-legislacao")
@Tag(name = "Parâmetros de Legislação", description = "APIs para gerenciamento de parâmetros de legislação")
public class ParametrosLegislacaoController {

    @Autowired
    private ParametrosLegislacaoRepository parametrosLegislacaoRepository;

    @GetMapping
    @Operation(summary = "Listar todos os parâmetros", description = "Retorna uma lista de todos os parâmetros de legislação cadastrados")
    public List<ParametrosLegislacao> getAllParametrosLegislacao() {
        return parametrosLegislacaoRepository.findAll();
    }

    @GetMapping("/filtros")
    @Operation(summary = "Obter parâmetros para criar os checkboxes no front na tela de qualidade", description = "API usada no front para trazer todos parâmetros de legislações para criar filtros. Retorna apenas um registro por id_analise (os que repetem aparecem uma vez)")
    public List<Map<String, Object>> getParametrosParaFiltros() {
        return parametrosLegislacaoRepository.findParametrosParaFiltros();
    }

    @GetMapping("/busca-dados-relacionados")
    @Operation(summary = "Obter dados relacionados de legislação e análise", description = "Consulta todos os dados relacionados na tabela parametros_legislacao, incluindo nome e símbolo da análise e nome da legislação. Útil para o front entender os relacionamentos sem precisar buscar IDs separadamente.")
    public List<Map<String, Object>> getDadosRelacionados() {
        return parametrosLegislacaoRepository.findDadosRelacionados();
    }
}
