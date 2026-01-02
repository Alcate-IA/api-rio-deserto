package com.rioDesertoAcessoDb.service;

import com.rioDesertoAcessoDb.model.AvaliacaoAnaliseIaQualidadeAgua;
import com.rioDesertoAcessoDb.repositories.AvaliacaoAnaliseIaQualidadeAguaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AvaliacaoAnaliseIaQualidadeAguaService {

    @Autowired
    private AvaliacaoAnaliseIaQualidadeAguaRepository repository;

    public Optional<AvaliacaoAnaliseIaQualidadeAgua> findById(Integer id) {
        return repository.findById(id);
    }

    public List<AvaliacaoAnaliseIaQualidadeAgua> findAll() {
        return repository.findAll();
    }

    public List<AvaliacaoAnaliseIaQualidadeAgua> findByIdZeus(Integer idZeus) {
        return repository.findByIdZeus(idZeus);
    }

    public AvaliacaoAnaliseIaQualidadeAgua save(AvaliacaoAnaliseIaQualidadeAgua avaliacao) {
        return repository.save(avaliacao);
    }

    public List<AvaliacaoAnaliseIaQualidadeAgua> findNaoAnalisadas() {
        return repository.findByIaAnalisouFalse();
    }
}
