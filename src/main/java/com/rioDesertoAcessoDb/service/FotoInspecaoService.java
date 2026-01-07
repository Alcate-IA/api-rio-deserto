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

    /**
     * Busca uma foto de inspeção pelo seu ID.
     * 
     * @param idFoto ID da foto a ser buscada.
     * @return A foto encontrada ou null se não existir.
     */
    public FotoInspecao buscarPorId(Integer idFoto) {
        return repositorio.findById(idFoto).orElse(null);
    }
}
