package com.rioDesertoAcessoDb.repositories;

import com.rioDesertoAcessoDb.model.AcaoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcaoUsuarioRepository extends JpaRepository<AcaoUsuario, Integer> {

    List<AcaoUsuario> findByUsuarioIdOrderByTimestampDesc(String usuarioId);
}