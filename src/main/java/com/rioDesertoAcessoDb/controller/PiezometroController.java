package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.model.Piezometro;
import com.rioDesertoAcessoDb.dtos.PiezometroAtivoDTO;
import com.rioDesertoAcessoDb.repositories.PiezometroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/piezometros")
public class PiezometroController {

    @Autowired
    private PiezometroRepository piezometroRepository;

    @GetMapping
    public List<Piezometro> getAllPiezometros() {
        return piezometroRepository.findAll();
    }

    @GetMapping("/ativos")
    public List<PiezometroAtivoDTO> getPiezometrosAtivosComDTO(
            @RequestParam(required = false) List<String> tipos) {

        if (tipos != null && !tipos.isEmpty()) {
            return piezometroRepository.findAtivosPorTiposSimplificadoDTO(tipos);
        } else {
            return piezometroRepository.findAtivosSimplificadoDTO();
        }
    }
}