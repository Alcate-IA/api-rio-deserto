package com.rioDesertoAcessoDb.repositories;

import com.rioDesertoAcessoDb.model.FotoInspecao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FotoInspecaoRepository extends JpaRepository<FotoInspecao, Integer> {
    List<FotoInspecao> findByCdPiezometro(Integer cdPiezometro);

    List<FotoInspecao> findByCdPiezometroAndDataInsercaoBetween(Integer cdPiezometro, LocalDateTime inicio,
            LocalDateTime fim);
}
