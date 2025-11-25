package com.rioDesertoAcessoDb.dtos;

public class PiezometroAtivoDTO {
    private Integer cdPiezometro;
    private String idPiezometro;
    private String nomePiezometro;
    private String situacaoPiezometro;

    public PiezometroAtivoDTO(Integer cdPiezometro, String idPiezometro, String nomePiezometro, String situacaoPiezometro) {
        this.cdPiezometro = cdPiezometro;
        this.idPiezometro = idPiezometro;
        this.nomePiezometro = nomePiezometro;
        this.situacaoPiezometro = situacaoPiezometro;
    }

    public Integer getCdPiezometro() {
        return cdPiezometro;
    }

    public void setCdPiezometro(Integer cdPiezometro) {
        this.cdPiezometro = cdPiezometro;
    }

    public String getIdPiezometro() {
        return idPiezometro;
    }

    public void setIdPiezometro(String idPiezometro) {
        this.idPiezometro = idPiezometro;
    }

    public String getNomePiezometro() {
        return nomePiezometro;
    }

    public void setNomePiezometro(String nomePiezometro) {
        this.nomePiezometro = nomePiezometro;
    }

    public String getSituacaoPiezometro() {
        return situacaoPiezometro;
    }

    public void setSituacaoPiezometro(String situacaoPiezometro) {
        this.situacaoPiezometro = situacaoPiezometro;
    }
}