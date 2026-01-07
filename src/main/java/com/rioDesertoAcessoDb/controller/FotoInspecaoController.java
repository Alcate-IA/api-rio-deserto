package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.model.FotoInspecao;
import com.rioDesertoAcessoDb.service.FotoInspecaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{idFoto}")
    @Operation(summary = "Buscar foto de inspeção por ID", description = "Retorna uma foto de inspeção específica com base no seu ID único")
    @ApiResponse(responseCode = "200", description = "Foto encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Foto não encontrada")
    public ResponseEntity<FotoInspecao> buscarPorId(@PathVariable Integer idFoto) {
        FotoInspecao foto = servico.buscarPorId(idFoto);
        if (foto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foto);
    }
}
