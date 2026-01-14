package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.dtos.DelayedPiezometerDTO;
import com.rioDesertoAcessoDb.service.InspecaoPiezometroFreqService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inspecao-piezometro-freq")
@Tag(name = "Inspeção Piezômetro Frequência", description = "APIs para controle de frequência de inspeção de piezômetros")
public class InspecaoPiezometroFreqController {

    @Autowired
    private InspecaoPiezometroFreqService service;

    @GetMapping("/atrasados")
    @Operation(summary = "Retorna os piezômetros com inspeção atrasada", description = "Identifica piezômetros ativos da empresa 18 que estão com inspeção fora do prazo baseado na frequência configurada.")
    public List<DelayedPiezometerDTO> getAtrasados() {
        return service.getPiezometrosAtrasados(18); // Empresa 18 conforme solicitado
    }
}
