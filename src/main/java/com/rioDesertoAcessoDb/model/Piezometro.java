package com.rioDesertoAcessoDb.model;

import java.time.LocalDateTime;

public class Piezometro {
    private Integer cdPiezometro;
    private Integer cdEmpresa;
    private String idPiezometro;
    private String nmPiezometro;
    private String tpPiezometro;
    private String dsObservacao;
    private String fgSituacao;
    private Integer cdUsuarioInclusao;
    private LocalDateTime dtInclusao;
    private Integer cdUsuarioAlteracao;
    private LocalDateTime dtAlteracao;
    private Integer cdBacia;

    public Integer getCdPiezometro() {
        return cdPiezometro;
    }

    public void setCdPiezometro(Integer cdPiezometro) {
        this.cdPiezometro = cdPiezometro;
    }

    public Integer getCdEmpresa() {
        return cdEmpresa;
    }

    public void setCdEmpresa(Integer cdEmpresa) {
        this.cdEmpresa = cdEmpresa;
    }

    public String getIdPiezometro() {
        return idPiezometro;
    }

    public void setIdPiezometro(String idPiezometro) {
        this.idPiezometro = idPiezometro;
    }

    public String getNmPiezometro() {
        return nmPiezometro;
    }

    public void setNmPiezometro(String nmPiezometro) {
        this.nmPiezometro = nmPiezometro;
    }

    public String getTpPiezometro() {
        return tpPiezometro;
    }

    public void setTpPiezometro(String tpPiezometro) {
        this.tpPiezometro = tpPiezometro;
    }

    public String getDsObservacao() {
        return dsObservacao;
    }

    public void setDsObservacao(String dsObservacao) {
        this.dsObservacao = dsObservacao;
    }

    public String getFgSituacao() {
        return fgSituacao;
    }

    public void setFgSituacao(String fgSituacao) {
        this.fgSituacao = fgSituacao;
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

    public Integer getCdBacia() {
        return cdBacia;
    }

    public void setCdBacia(Integer cdBacia) {
        this.cdBacia = cdBacia;
    }
}