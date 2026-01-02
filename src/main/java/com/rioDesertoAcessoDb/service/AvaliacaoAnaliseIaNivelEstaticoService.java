package com.rioDesertoAcessoDb.service;

import com.rioDesertoAcessoDb.model.AvaliacaoAnaliseIaNivelEstatico;
import com.rioDesertoAcessoDb.repositories.AvaliacaoAnaliseIaNivelEstaticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AvaliacaoAnaliseIaNivelEstaticoService {

    @Autowired
    private AvaliacaoAnaliseIaNivelEstaticoRepository repository;

    public Optional<AvaliacaoAnaliseIaNivelEstatico> findById(Integer id) {
        return repository.findById(id);
    }

    public List<AvaliacaoAnaliseIaNivelEstatico> findAll() {
        return repository.findAll();
    }

    public List<AvaliacaoAnaliseIaNivelEstatico> findByCdPiezometro(Integer cdPiezometro) {
        return repository.findByCdPiezometro(cdPiezometro);
    }

    public AvaliacaoAnaliseIaNivelEstatico save(AvaliacaoAnaliseIaNivelEstatico avaliacao) {
        return repository.save(avaliacao);
    }

    public List<AvaliacaoAnaliseIaNivelEstatico> findNaoAnalisadas() {
        return repository.findByIaAnalisouFalse();
    }
}
