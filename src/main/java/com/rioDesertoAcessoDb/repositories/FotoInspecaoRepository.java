package com.rioDesertoAcessoDb.repositories;

import com.rioDesertoAcessoDb.model.FotoInspecao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoInspecaoRepository extends JpaRepository<FotoInspecao, Integer> {
}
