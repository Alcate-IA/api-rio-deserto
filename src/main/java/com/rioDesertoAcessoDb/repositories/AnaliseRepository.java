package com.rioDesertoAcessoDb.repositories;

import com.rioDesertoAcessoDb.model.Analise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnaliseRepository extends JpaRepository<Analise, Integer> {
    List<Analise> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);

    List<Analise> findBySimboloContainingIgnoreCaseOrderByNomeAsc(String simbolo);
}