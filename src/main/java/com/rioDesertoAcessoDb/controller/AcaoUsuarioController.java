package com.rioDesertoAcessoDb.controller;

import com.rioDesertoAcessoDb.dtos.AcaoUsuarioDTO;
import com.rioDesertoAcessoDb.model.AcaoUsuario;
import com.rioDesertoAcessoDb.service.AcaoUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/acoes-usuario")
@Tag(name = "Ações do Usuário", description = "APIs para registrar e consultar ações de usuários")
public class AcaoUsuarioController {

    private final AcaoUsuarioService acaoUsuarioService;

    @Autowired
    public AcaoUsuarioController(AcaoUsuarioService acaoUsuarioService) {
        this.acaoUsuarioService = acaoUsuarioService;
    }

    @PostMapping
    @Operation(summary = "Registrar uma nova ação de usuário", description = "Cria um novo registro de ação para um usuário.")
    @ApiResponse(responseCode = "201", description = "Ação registrada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    public ResponseEntity<AcaoUsuario> registrarAcao(@Valid @RequestBody AcaoUsuarioDTO acaoDto) {
        AcaoUsuario novaAcao = acaoUsuarioService.salvarAcao(acaoDto);
        return new ResponseEntity<>(novaAcao, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todas as ações de usuários", description = "Retorna uma lista com todas as ações de todos os usuários.")
    public List<AcaoUsuario> listarTodasAcoes() {
        return acaoUsuarioService.listarTodasAcoes();
    }

    @GetMapping("/{usuarioId}")
    @Operation(summary = "Listar ações por ID de usuário", description = "Retorna todas as ações registradas para um usuário específico, ordenadas da mais recente para a mais antiga.")
    public List<AcaoUsuario> listarAcoesPorUsuario(@PathVariable String usuarioId) {
        return acaoUsuarioService.listarAcoesPorUsuario(usuarioId);
    }
}