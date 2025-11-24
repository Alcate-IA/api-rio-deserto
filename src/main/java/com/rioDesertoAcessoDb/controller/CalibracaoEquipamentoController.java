package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.model.CalibracaoEquipamento;
import com.rioDesertoAcessoDb.repositories.CalibracaoEquipamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calibracoes")
public class CalibracaoEquipamentoController {

    @Autowired
    private CalibracaoEquipamentoRepository calibracaoRepository;

    @GetMapping
    public List<CalibracaoEquipamento> getAllCalibracoes() {
        return calibracaoRepository.findAll();
    }
}