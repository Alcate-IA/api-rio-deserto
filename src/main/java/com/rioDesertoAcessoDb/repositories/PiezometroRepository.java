package com.rioDesertoAcessoDb.repositories;

import com.rioDesertoAcessoDb.model.Piezometro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PiezometroRepository extends JpaRepository<Piezometro, Integer>, JpaSpecificationExecutor<Piezometro> {

    @Query("SELECT p.tpPiezometro FROM Piezometro p WHERE p.cdPiezometro = :cdPiezometro")
    String findTipoPiezometroById(@Param("cdPiezometro") Integer cdPiezometro);

    @Query("SELECT p.idPiezometro FROM Piezometro p WHERE p.cdPiezometro = :cdPiezometro")
    String findIdPiezometroByCdPiezometro(@Param("cdPiezometro") Integer cdPiezometro);
}