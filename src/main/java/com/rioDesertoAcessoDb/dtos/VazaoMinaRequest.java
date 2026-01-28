package com.rioDesertoAcessoDb.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VazaoMinaRequest {

    @NotNull(message = "mes é obrigatório")
    @Min(value = 1, message = "mes deve ser entre 1 e 12")
    @Max(value = 12, message = "mes deve ser entre 1 e 12")
    private Integer mes;

    @NotNull(message = "ano é obrigatório")
    @Min(value = 1900, message = "ano inválido")
    @Max(value = 2100, message = "ano inválido")
    private Integer ano;

    @NotNull(message = "vazao_bombeamento é obrigatório")
    @DecimalMin(value = "0", inclusive = true, message = "vazao_bombeamento deve ser maior ou igual a zero")
    private BigDecimal vazaoBombeamento;
}
