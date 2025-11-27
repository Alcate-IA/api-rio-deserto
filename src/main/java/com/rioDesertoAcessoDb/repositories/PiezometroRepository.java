package com.rioDesertoAcessoDb.repositories;

import com.rioDesertoAcessoDb.model.Piezometro;
import com.rioDesertoAcessoDb.dtos.PiezometroAtivoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PiezometroRepository extends JpaRepository<Piezometro, Integer> {

    @Query("SELECT new com.rioDesertoAcessoDb.dtos.PiezometroAtivoDTO(p.cdPiezometro, p.idPiezometro, p.nmPiezometro, p.fgSituacao, p.tpPiezometro) FROM Piezometro p WHERE p.fgSituacao = 'A' AND p.cdEmpresa = 18")
    List<PiezometroAtivoDTO> findAtivosSimplificadoDTO();

    @Query("SELECT new com.rioDesertoAcessoDb.dtos.PiezometroAtivoDTO(p.cdPiezometro, p.idPiezometro, p.nmPiezometro, p.fgSituacao, p.tpPiezometro) FROM Piezometro p WHERE p.fgSituacao = 'A' AND p.cdEmpresa = 18 AND p.tpPiezometro IN :tipos")
    List<PiezometroAtivoDTO> findAtivosPorTiposSimplificadoDTO(@Param("tipos") List<String> tipos);

    @Query("SELECT p.tpPiezometro FROM Piezometro p WHERE p.cdPiezometro = :cdPiezometro")
    String findTipoPiezometroById(@Param("cdPiezometro") Integer cdPiezometro);
}