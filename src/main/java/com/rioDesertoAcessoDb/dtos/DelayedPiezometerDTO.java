package com.rioDesertoAcessoDb.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO que representa um piezômetro com inspeção atrasada ou em dia")
public class DelayedPiezometerDTO {

    @Schema(description = "Código interno do piezômetro", example = "84")
    private Integer cdPiezometro;

    @Schema(description = "Nome do piezômetro", example = "Poço Fazenda Esperança 01")
    private String nmPiezometro;

    @Schema(description = "Descrição da frequência de inspeção", example = "TRIMESTRAL")
    private String frequenciaDescricao;

    @Schema(description = "Data da última inspeção realizada")
    private LocalDateTime dtUltimaInspecao;

    @Schema(description = "Tabela de origem da última inspeção", example = "TB_INSPECAO_PIEZOMETRO_MVTO")
    private String origem;

    @Schema(description = "Situação da inspeção", example = "atrasado")
    private String situacao; // "atrasado" ou "em dia"

    @Schema(description = "Quantidade de dias de atraso", example = "5")
    private Long diasAtrasado;
}
