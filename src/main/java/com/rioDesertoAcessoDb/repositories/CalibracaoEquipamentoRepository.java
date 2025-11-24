package com.rioDesertoAcessoDb.repositories;

import com.rioDesertoAcessoDb.model.CalibracaoEquipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalibracaoEquipamentoRepository extends JpaRepository<CalibracaoEquipamento, Integer> {


}