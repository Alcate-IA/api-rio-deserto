package com.rioDesertoAcessoDb.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AcaoUsuarioDTO {

    @NotBlank(message = "O ID do usuário não pode ser vazio.")
    @Size(max = 100, message = "O ID do usuário não pode exceder 100 caracteres.")
    private String usuarioId;

    @NotBlank(message = "A ação não pode ser vazia.")
    @Size(max = 255, message = "A ação não pode exceder 255 caracteres.")
    private String acao;
}