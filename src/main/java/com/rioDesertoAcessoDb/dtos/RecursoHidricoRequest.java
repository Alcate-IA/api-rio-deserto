package com.rioDesertoAcessoDb.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RecursoHidricoRequest {
    @NotNull(message = "cdPiezometro é obrigatório")
    private Integer cdPiezometro;

    @NotNull(message = "dtInspecao é obrigatório")
    private String dtInspecao;

    @NotNull(message = "qtVazao é obrigatório")
    private Double qtVazao;

    private String observacao;

    private String coletor;
}