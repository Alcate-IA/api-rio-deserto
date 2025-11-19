package com.rioDesertoAcessoDb.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class NivelAguaItem {
    private Integer cdNivelAguaItem;
    private Integer cdNivelAgua;
    private LocalDate dtInspecao;
    private String nrMatriculaInspecao;
    private String nmColaboradorInspecao;
    private BigDecimal vlPrecipitacao;
    private BigDecimal qtLeitura;
    private BigDecimal qtNivelEstatico;
    private String fgRac;
    private String nrRac;
    private String dsObservacao;
    private Integer cdUsuarioInclusao;
    private LocalDateTime dtInclusao;
    private Integer cdUsuarioAlteracao;
    private LocalDateTime dtAlteracao;
    private LocalDate dtAvaliacao;
    private String nrMatriculaAvaliacao;
    private String nmColaboradorAvaliacao;

    public Integer getCdNivelAguaItem() {
        return cdNivelAguaItem;
    }

    public void setCdNivelAguaItem(Integer cdNivelAguaItem) {
        this.cdNivelAguaItem = cdNivelAguaItem;
    }

    public Integer getCdNivelAgua() {
        return cdNivelAgua;
    }

    public void setCdNivelAgua(Integer cdNivelAgua) {
        this.cdNivelAgua = cdNivelAgua;
    }

    public LocalDate getDtInspecao() {
        return dtInspecao;
    }

    public void setDtInspecao(LocalDate dtInspecao) {
        this.dtInspecao = dtInspecao;
    }

    public String getNrMatriculaInspecao() {
        return nrMatriculaInspecao;
    }

    public void setNrMatriculaInspecao(String nrMatriculaInspecao) {
        this.nrMatriculaInspecao = nrMatriculaInspecao;
    }

    public String getNmColaboradorInspecao() {
        return nmColaboradorInspecao;
    }

    public void setNmColaboradorInspecao(String nmColaboradorInspecao) {
        this.nmColaboradorInspecao = nmColaboradorInspecao;
    }

    public BigDecimal getVlPrecipitacao() {
        return vlPrecipitacao;
    }

    public void setVlPrecipitacao(BigDecimal vlPrecipitacao) {
        this.vlPrecipitacao = vlPrecipitacao;
    }

    public BigDecimal getQtLeitura() {
        return qtLeitura;
    }

    public void setQtLeitura(BigDecimal qtLeitura) {
        this.qtLeitura = qtLeitura;
    }

    public BigDecimal getQtNivelEstatico() {
        return qtNivelEstatico;
    }

    public void setQtNivelEstatico(BigDecimal qtNivelEstatico) {
        this.qtNivelEstatico = qtNivelEstatico;
    }

    public String getFgRac() {
        return fgRac;
    }

    public void setFgRac(String fgRac) {
        this.fgRac = fgRac;
    }

    public String getNrRac() {
        return nrRac;
    }

    public void setNrRac(String nrRac) {
        this.nrRac = nrRac;
    }

    public String getDsObservacao() {
        return dsObservacao;
    }

    public void setDsObservacao(String dsObservacao) {
        this.dsObservacao = dsObservacao;
    }

    public Integer getCdUsuarioInclusao() {
        return cdUsuarioInclusao;
    }

    public void setCdUsuarioInclusao(Integer cdUsuarioInclusao) {
        this.cdUsuarioInclusao = cdUsuarioInclusao;
    }

    public LocalDateTime getDtInclusao() {
        return dtInclusao;
    }

    public void setDtInclusao(LocalDateTime dtInclusao) {
        this.dtInclusao = dtInclusao;
    }

    public Integer getCdUsuarioAlteracao() {
        return cdUsuarioAlteracao;
    }

    public void setCdUsuarioAlteracao(Integer cdUsuarioAlteracao) {
        this.cdUsuarioAlteracao = cdUsuarioAlteracao;
    }

    public LocalDateTime getDtAlteracao() {
        return dtAlteracao;
    }

    public void setDtAlteracao(LocalDateTime dtAlteracao) {
        this.dtAlteracao = dtAlteracao;
    }

    public LocalDate getDtAvaliacao() {
        return dtAvaliacao;
    }

    public void setDtAvaliacao(LocalDate dtAvaliacao) {
        this.dtAvaliacao = dtAvaliacao;
    }

    public String getNrMatriculaAvaliacao() {
        return nrMatriculaAvaliacao;
    }

    public void setNrMatriculaAvaliacao(String nrMatriculaAvaliacao) {
        this.nrMatriculaAvaliacao = nrMatriculaAvaliacao;
    }

    public String getNmColaboradorAvaliacao() {
        return nmColaboradorAvaliacao;
    }

    public void setNmColaboradorAvaliacao(String nmColaboradorAvaliacao) {
        this.nmColaboradorAvaliacao = nmColaboradorAvaliacao;
    }
}