package com.rioDesertoAcessoDb.service;

import com.rioDesertoAcessoDb.model.AvaliacaoAnaliseIaQualidadeAgua;
import com.rioDesertoAcessoDb.repositories.AvaliacaoAnaliseIaQualidadeAguaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliacaoAnaliseIaQualidadeAguaService {

    @Autowired
    private AvaliacaoAnaliseIaQualidadeAguaRepository repository;

    public List<AvaliacaoAnaliseIaQualidadeAgua> findAll() {
        return repository.findAll();
    }

    public List<AvaliacaoAnaliseIaQualidadeAgua> findByIdZeus(Integer idZeus) {
        return repository.findByIdZeus(idZeus);
    }

    public AvaliacaoAnaliseIaQualidadeAgua save(AvaliacaoAnaliseIaQualidadeAgua avaliacao) {
        return repository.save(avaliacao);
    }
}
