package com.rioDesertoAcessoDb.service;

import com.rioDesertoAcessoDb.dtos.AcaoUsuarioDTO;
import com.rioDesertoAcessoDb.model.AcaoUsuario;
import com.rioDesertoAcessoDb.repositories.AcaoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcaoUsuarioService {

    private final AcaoUsuarioRepository acaoUsuarioRepository;

    @Autowired
    public AcaoUsuarioService(AcaoUsuarioRepository acaoUsuarioRepository) {
        this.acaoUsuarioRepository = acaoUsuarioRepository;
    }

    public AcaoUsuario salvarAcao(AcaoUsuarioDTO acaoDto) {
        AcaoUsuario acaoUsuario = new AcaoUsuario();
        acaoUsuario.setUsuarioId(acaoDto.getUsuarioId());
        acaoUsuario.setAcao(acaoDto.getAcao());
        return acaoUsuarioRepository.save(acaoUsuario);
    }

    public List<AcaoUsuario> listarTodasAcoes() {
        return acaoUsuarioRepository.findAll();
    }

    public List<AcaoUsuario> listarAcoesPorUsuario(String usuarioId) {
        return acaoUsuarioRepository.findByUsuarioIdOrderByTimestampDesc(usuarioId);
    }
}