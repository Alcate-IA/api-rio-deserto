package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.model.Piezometro;
import com.rioDesertoAcessoDb.service.PiezometroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/piezometros")
@Tag(name = "Piezômetros", description = "APIs para consulta e gerenciamento de piezômetros")
public class PiezometroController {

    @Autowired
    private PiezometroService piezometroService;

    @GetMapping
    @Operation(summary = "Listar piezômetros com filtros", description = "Retorna uma lista de piezômetros da mina 101 (cd_empresa 18), permitindo filtrar por situação e tipo")
    public List<Piezometro> getPiezometros(
            @Parameter(description = "Situação do piezômetro ('A' para Ativo, 'I' para Inativo)", example = "A") @RequestParam(required = false) String situacao,
            @Parameter(description = "Lista de tipos de piezômetro (PP, PR, PC, PV, PB)", example = "PP,PR") @RequestParam(required = false) List<String> tipos) {
        return piezometroService.getPiezometrosComFiltros(situacao, tipos);
    }
}