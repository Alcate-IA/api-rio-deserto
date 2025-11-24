package com.rioDesertoAcessoDb.repositories;

import com.rioDesertoAcessoDb.model.Piezometro;
import com.rioDesertoAcessoDb.dtos.PiezometroAtivoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PiezometroRepository extends JpaRepository<Piezometro, Integer> {

    @Query("SELECT new com.rioDesertoAcessoDb.dtos.PiezometroAtivoDTO(p.idPiezometro, p.nmPiezometro, p.fgSituacao) FROM Piezometro p WHERE p.fgSituacao = 'A'")
    List<PiezometroAtivoDTO> findAtivosSimplificadoDTO();
}