package com.rioDesertoAcessoDb.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class RecursosHidricosItem {
    private Integer cdRecursosHidricosItem;
    private Integer cdRecursosHidricos;
    private LocalDate dtInspecao;
    private String nrMatriculaInspecao;
    private String nmColaboradorInspecao;
    private Integer cdCalibracaoEquipamento;
    private BigDecimal vlPrecipitacao;
    private BigDecimal qtLeitura;
    private String dsObservacao;
    private Integer cdUsuarioInclusao;
    private LocalDateTime dtInclusao;
    private Integer cdUsuarioAlteracao;
    private LocalDateTime dtAlteracao;
    private LocalDate dtAvaliacao;
    private String nrMatriculaAvaliacao;
    private String nmColaboradorAvaliacao;

    public Integer getCdRecursosHidricosItem() {
        return cdRecursosHidricosItem;
    }

    public void setCdRecursosHidricosItem(Integer cdRecursosHidricosItem) {
        this.cdRecursosHidricosItem = cdRecursosHidricosItem;
    }

    public Integer getCdRecursosHidricos() {
        return cdRecursosHidricos;
    }

    public void setCdRecursosHidricos(Integer cdRecursosHidricos) {
        this.cdRecursosHidricos = cdRecursosHidricos;
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

    public Integer getCdCalibracaoEquipamento() {
        return cdCalibracaoEquipamento;
    }

    public void setCdCalibracaoEquipamento(Integer cdCalibracaoEquipamento) {
        this.cdCalibracaoEquipamento = cdCalibracaoEquipamento;
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