package com.rioDesertoAcessoDb.repositories;

import com.rioDesertoAcessoDb.model.AvaliacaoAnaliseIaNivelEstatico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoAnaliseIaNivelEstaticoRepository
        extends JpaRepository<AvaliacaoAnaliseIaNivelEstatico, Integer> {
    List<AvaliacaoAnaliseIaNivelEstatico> findByCdPiezometro(Integer cdPiezometro);

    List<AvaliacaoAnaliseIaNivelEstatico> findByIaAnalisouFalse();
}
