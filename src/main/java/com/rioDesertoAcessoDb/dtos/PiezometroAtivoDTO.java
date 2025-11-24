package com.rioDesertoAcessoDb.dtos;

public class PiezometroAtivoDTO {
    private String idPiezometro;
    private String nomePiezometro;
    private String situacaoPiezometro;

    public PiezometroAtivoDTO(String idPiezometro, String nomePiezometro, String situacaoPiezometro) {
        this.idPiezometro = idPiezometro;
        this.nomePiezometro = nomePiezometro;
        this.situacaoPiezometro = situacaoPiezometro;
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