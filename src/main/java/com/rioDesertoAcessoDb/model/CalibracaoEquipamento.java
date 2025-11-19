package com.rioDesertoAcessoDb.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// REMOVI todas as anotações JPA - é apenas um POJO simples agora
public class CalibracaoEquipamento {

    private Integer cdCalibracaoEquipamento;
    private Integer cdEmpresa;
    private Integer cdEmpresaSetor;
    private Integer cdOrgao;
    private Integer cdUnidadeMedidaEquipamento;
    private String nrEquipamento;
    private String dsEquipamento;
    private String dsMarca;
    private BigDecimal vlMenorDivisao;
    private BigDecimal vlFaixaIndicacaoInicial;
    private BigDecimal vlFaixaIndicacaoFinal;
    private BigDecimal vlExatidaoRequerida;
    private Integer qtFrequenciaCalibracaoMeses;
    private Integer cdUsuarioInclusao;
    private LocalDateTime dtInclusao;
    private Integer cdUsuarioAlteracao;
    private LocalDateTime dtAlteracao;
    private String dsObservacao;
    private Integer cdEquipamento;
    private String fgSituacao;
    private String vlFaixaIndicacao;
    private String vlFaixaUsoEquip;

    // ========================
    // Construtores
    // ========================

    public CalibracaoEquipamento() {
        // Construtor padrão necessário
    }

    // ========================
    // Getters e Setters
    // ========================

    public Integer getCdCalibracaoEquipamento() {
        return cdCalibracaoEquipamento;
    }

    public void setCdCalibracaoEquipamento(Integer cdCalibracaoEquipamento) {
        this.cdCalibracaoEquipamento = cdCalibracaoEquipamento;
    }

    public Integer getCdEmpresa() {
        return cdEmpresa;
    }

    public void setCdEmpresa(Integer cdEmpresa) {
        this.cdEmpresa = cdEmpresa;
    }

    public Integer getCdEmpresaSetor() {
        return cdEmpresaSetor;
    }

    public void setCdEmpresaSetor(Integer cdEmpresaSetor) {
        this.cdEmpresaSetor = cdEmpresaSetor;
    }

    public Integer getCdOrgao() {
        return cdOrgao;
    }

    public void setCdOrgao(Integer cdOrgao) {
        this.cdOrgao = cdOrgao;
    }

    public Integer getCdUnidadeMedidaEquipamento() {
        return cdUnidadeMedidaEquipamento;
    }

    public void setCdUnidadeMedidaEquipamento(Integer cdUnidadeMedidaEquipamento) {
        this.cdUnidadeMedidaEquipamento = cdUnidadeMedidaEquipamento;
    }

    public String getNrEquipamento() {
        return nrEquipamento;
    }

    public void setNrEquipamento(String nrEquipamento) {
        this.nrEquipamento = nrEquipamento;
    }

    public String getDsEquipamento() {
        return dsEquipamento;
    }

    public void setDsEquipamento(String dsEquipamento) {
        this.dsEquipamento = dsEquipamento;
    }

    public String getDsMarca() {
        return dsMarca;
    }

    public void setDsMarca(String dsMarca) {
        this.dsMarca = dsMarca;
    }

    public BigDecimal getVlMenorDivisao() {
        return vlMenorDivisao;
    }

    public void setVlMenorDivisao(BigDecimal vlMenorDivisao) {
        this.vlMenorDivisao = vlMenorDivisao;
    }

    public BigDecimal getVlFaixaIndicacaoInicial() {
        return vlFaixaIndicacaoInicial;
    }

    public void setVlFaixaIndicacaoInicial(BigDecimal vlFaixaIndicacaoInicial) {
        this.vlFaixaIndicacaoInicial = vlFaixaIndicacaoInicial;
    }

    public BigDecimal getVlFaixaIndicacaoFinal() {
        return vlFaixaIndicacaoFinal;
    }

    public void setVlFaixaIndicacaoFinal(BigDecimal vlFaixaIndicacaoFinal) {
        this.vlFaixaIndicacaoFinal = vlFaixaIndicacaoFinal;
    }

    public BigDecimal getVlExatidaoRequerida() {
        return vlExatidaoRequerida;
    }

    public void setVlExatidaoRequerida(BigDecimal vlExatidaoRequerida) {
        this.vlExatidaoRequerida = vlExatidaoRequerida;
    }

    public Integer getQtFrequenciaCalibracaoMeses() {
        return qtFrequenciaCalibracaoMeses;
    }

    public void setQtFrequenciaCalibracaoMeses(Integer qtFrequenciaCalibracaoMeses) {
        this.qtFrequenciaCalibracaoMeses = qtFrequenciaCalibracaoMeses;
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

    public Integer getCdEquipamento() {
        return cdEquipamento;
    }

    public void setCdEquipamento(Integer cdEquipamento) {
        this.cdEquipamento = cdEquipamento;
    }

    public String getFgSituacao() {
        return fgSituacao;
    }

    public void setFgSituacao(String fgSituacao) {
        this.fgSituacao = fgSituacao;
    }

    public String getVlFaixaIndicacao() {
        return vlFaixaIndicacao;
    }

    public void setVlFaixaIndicacao(String vlFaixaIndicacao) {
        this.vlFaixaIndicacao = vlFaixaIndicacao;
    }

    public String getVlFaixaUsoEquip() {
        return vlFaixaUsoEquip;
    }

    public void setVlFaixaUsoEquip(String vlFaixaUsoEquip) {
        this.vlFaixaUsoEquip = vlFaixaUsoEquip;
    }

    @Override
    public String toString() {
        return "CalibracaoEquipamento{" +
                "cdCalibracaoEquipamento=" + cdCalibracaoEquipamento +
                ", nrEquipamento='" + nrEquipamento + '\'' +
                ", dsEquipamento='" + dsEquipamento + '\'' +
                ", fgSituacao='" + fgSituacao + '\'' +
                '}';
    }
}