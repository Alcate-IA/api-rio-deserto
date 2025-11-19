package com.rioDesertoAcessoDb.model;

import java.time.LocalDateTime;

public class Meteorologia {
    private Integer cdMeteorologia;
    private Integer cdEmpresa;
    private String nrMatricula;
    private String nmColaborador;
    private Integer cdUsuarioInclusao;
    private LocalDateTime dtInclusao;
    private Integer cdUsuarioAlteracao;
    private LocalDateTime dtAlteracao;
    private Integer cdCalibracaoEquipamento;

    public Integer getCdMeteorologia() {
        return cdMeteorologia;
    }

    public void setCdMeteorologia(Integer cdMeteorologia) {
        this.cdMeteorologia = cdMeteorologia;
    }

    public Integer getCdEmpresa() {
        return cdEmpresa;
    }

    public void setCdEmpresa(Integer cdEmpresa) {
        this.cdEmpresa = cdEmpresa;
    }

    public String getNrMatricula() {
        return nrMatricula;
    }

    public void setNrMatricula(String nrMatricula) {
        this.nrMatricula = nrMatricula;
    }

    public String getNmColaborador() {
        return nmColaborador;
    }

    public void setNmColaborador(String nmColaborador) {
        this.nmColaborador = nmColaborador;
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

    public Integer getCdCalibracaoEquipamento() {
        return cdCalibracaoEquipamento;
    }

    public void setCdCalibracaoEquipamento(Integer cdCalibracaoEquipamento) {
        this.cdCalibracaoEquipamento = cdCalibracaoEquipamento;
    }
}