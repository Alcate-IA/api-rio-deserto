package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.model.FotoInspecao;
import com.rioDesertoAcessoDb.service.FotoInspecaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fotos-inspecao")
@Tag(name = "Fotos de Inspeção", description = "APIs para gerenciamento de fotos de inspeção de piezômetros")
public class FotoInspecaoController {

    @Autowired
    private FotoInspecaoService servico;

    @GetMapping
    @Operation(summary = "Listar todas as fotos de inspeção", description = "Retorna uma lista de todas as fotos de inspeção registradas")
    @ApiResponse(responseCode = "200", description = "Lista de fotos retornada com sucesso")
    public List<FotoInspecao> buscarTodas() {
        return servico.buscarTodas();
    }
}
