package com.rioDesertoAcessoDb.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Requisição para inserir inspeção de piezômetro")
public class InspecaoPiezometroRequest {

    @NotNull(message = "Código do piezômetro é obrigatório")
    @Schema(description = "Código do piezômetro", example = "907")
    private Integer cdPiezometro;

    @NotNull(message = "Data da inspeção é obrigatória")
    @Schema(description = "Data da inspeção (formato: dd.MM.yyyy ou dd/MM/yyyy)", example = "11.12.2025")
    private String dtInspecao;

    @NotNull(message = "Quantidade de leitura é obrigatória")
    @Schema(description = "Quantidade de leitura", example = "33.25")
    private Double qtLeitura;

    @NotNull(message = "Quantidade de nível estático é obrigatória")
    @Schema(description = "Quantidade de nível estático", example = "14.88")
    private Double qtNivelEstatico;

    // Construtores
    public InspecaoPiezometroRequest() {
    }

    public InspecaoPiezometroRequest(Integer cdPiezometro, String dtInspecao, Double qtLeitura,
            Double qtNivelEstatico) {
        this.cdPiezometro = cdPiezometro;
        this.dtInspecao = dtInspecao;
        this.qtLeitura = qtLeitura;
        this.qtNivelEstatico = qtNivelEstatico;
    }

    // Getters e Setters
    public Integer getCdPiezometro() {
        return cdPiezometro;
    }

    public void setCdPiezometro(Integer cdPiezometro) {
        this.cdPiezometro = cdPiezometro;
    }

    public String getDtInspecao() {
        return dtInspecao;
    }

    public void setDtInspecao(String dtInspecao) {
        this.dtInspecao = dtInspecao;
    }

    public Double getQtLeitura() {
        return qtLeitura;
    }

    public void setQtLeitura(Double qtLeitura) {
        this.qtLeitura = qtLeitura;
    }

    public Double getQtNivelEstatico() {
        return qtNivelEstatico;
    }

    public void setQtNivelEstatico(Double qtNivelEstatico) {
        this.qtNivelEstatico = qtNivelEstatico;
    }
}
