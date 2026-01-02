package com.rioDesertoAcessoDb.repositories;

import com.rioDesertoAcessoDb.model.AvaliacaoAnaliseIaQualidadeAgua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoAnaliseIaQualidadeAguaRepository
        extends JpaRepository<AvaliacaoAnaliseIaQualidadeAgua, Integer> {
    List<AvaliacaoAnaliseIaQualidadeAgua> findByIdZeus(Integer idZeus);

    List<AvaliacaoAnaliseIaQualidadeAgua> findByIaAnalisouFalse();
}
