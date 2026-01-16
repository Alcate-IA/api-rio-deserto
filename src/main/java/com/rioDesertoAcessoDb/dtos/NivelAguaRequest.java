package com.rioDesertoAcessoDb.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NivelAguaRequest {
    @NotNull(message = "cdPiezometro é obrigatório")
    private Integer cdPiezometro;

    @NotNull(message = "dtInspecao é obrigatório")
    private String dtInspecao;

    @NotNull(message = "qtNivelEstatico é obrigatório")
    private Double qtNivelEstatico;

    private String observacao;

    private String coletor;
}