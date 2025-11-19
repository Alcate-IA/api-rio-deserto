package com.rioDesertoAcessoDb.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RecursosHidricos {
    private Integer cdRecursosHidricos;
    private Integer cdEmpresa;
    private Integer cdPiezometro;
    private Integer cdUsuarioInclusao;
    private LocalDateTime dtInclusao;
    private Integer cdUsuarioAlteracao;
    private LocalDateTime dtAlteracao;
    private String dsObservacao;
    private String nrMatriculaResponsavel;
    private String nmResponsavel;
    private String fgSituacao;
    private BigDecimal vlCoordenadaN;
    private BigDecimal vlCoordenadaE;
    private String tpFrequencia;

    public Integer getCdRecursosHidricos() {
        return cdRecursosHidricos;
    }

    public void setCdRecursosHidricos(Integer cdRecursosHidricos) {
        this.cdRecursosHidricos = cdRecursosHidricos;
    }

    public Integer getCdEmpresa() {
        return cdEmpresa;
    }

    public void setCdEmpresa(Integer cdEmpresa) {
        this.cdEmpresa = cdEmpresa;
    }

    public Integer getCdPiezometro() {
        return cdPiezometro;
    }

    public void setCdPiezometro(Integer cdPiezometro) {
        this.cdPiezometro = cdPiezometro;
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

    public String getDsObservacao() {
        return dsObservacao;
    }

    public void setDsObservacao(String dsObservacao) {
        this.dsObservacao = dsObservacao;
    }

    public String getNrMatriculaResponsavel() {
        return nrMatriculaResponsavel;
    }

    public void setNrMatriculaResponsavel(String nrMatriculaResponsavel) {
        this.nrMatriculaResponsavel = nrMatriculaResponsavel;
    }

    public String getNmResponsavel() {
        return nmResponsavel;
    }

    public void setNmResponsavel(String nmResponsavel) {
        this.nmResponsavel = nmResponsavel;
    }

    public String getFgSituacao() {
        return fgSituacao;
    }

    public void setFgSituacao(String fgSituacao) {
        this.fgSituacao = fgSituacao;
    }

    public BigDecimal getVlCoordenadaN() {
        return vlCoordenadaN;
    }

    public void setVlCoordenadaN(BigDecimal vlCoordenadaN) {
        this.vlCoordenadaN = vlCoordenadaN;
    }

    public BigDecimal getVlCoordenadaE() {
        return vlCoordenadaE;
    }

    public void setVlCoordenadaE(BigDecimal vlCoordenadaE) {
        this.vlCoordenadaE = vlCoordenadaE;
    }

    public String getTpFrequencia() {
        return tpFrequencia;
    }

    public void setTpFrequencia(String tpFrequencia) {
        this.tpFrequencia = tpFrequencia;
    }
}