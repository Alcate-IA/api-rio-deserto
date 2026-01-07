package com.rioDesertoAcessoDb.service;

import com.rioDesertoAcessoDb.model.FotoInspecao;
import com.rioDesertoAcessoDb.repositories.FotoInspecaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FotoInspecaoService {

    @Autowired
    private FotoInspecaoRepository repositorio;

    /**
     * Busca todas as fotos de inspeção registradas.
     * 
     * @return Lista de todas as fotos de inspeção.
     */
    public List<FotoInspecao> buscarTodas() {
        return repositorio.findAll();
    }
}
