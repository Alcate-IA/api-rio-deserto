package com.rioDesertoAcessoDb.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class MeteorologiaItem {
    private Integer cdMeteorologiaItem;
    private Integer cdMeteorologia;
    private LocalDate dtItem;
    private LocalTime hrItem;
    private BigDecimal vlUmidade;
    private BigDecimal vlPressao;
    private BigDecimal vlTemperatura;
    private BigDecimal vlVento;
    private BigDecimal vlPrecipitacao;
    private String fgCondicaoTempo;
    private Integer cdUsuarioInclusao;
    private LocalDateTime dtInclusao;
    private Integer cdUsuarioAlteracao;
    private LocalDateTime dtAlteracao;
    private String fgRnc;
    private String nrRnc;
    private String dsObservacao;
    private String nrMatriculaResponsavel;
    private String nmResponsavel;
    private Integer cdCalibracaoEquipamento;

    public Integer getCdMeteorologiaItem() {
        return cdMeteorologiaItem;
    }

    public void setCdMeteorologiaItem(Integer cdMeteorologiaItem) {
        this.cdMeteorologiaItem = cdMeteorologiaItem;
    }

    public Integer getCdMeteorologia() {
        return cdMeteorologia;
    }

    public void setCdMeteorologia(Integer cdMeteorologia) {
        this.cdMeteorologia = cdMeteorologia;
    }

    public LocalDate getDtItem() {
        return dtItem;
    }

    public void setDtItem(LocalDate dtItem) {
        this.dtItem = dtItem;
    }

    public LocalTime getHrItem() {
        return hrItem;
    }

    public void setHrItem(LocalTime hrItem) {
        this.hrItem = hrItem;
    }

    public BigDecimal getVlUmidade() {
        return vlUmidade;
    }

    public void setVlUmidade(BigDecimal vlUmidade) {
        this.vlUmidade = vlUmidade;
    }

    public BigDecimal getVlPressao() {
        return vlPressao;
    }

    public void setVlPressao(BigDecimal vlPressao) {
        this.vlPressao = vlPressao;
    }

    public BigDecimal getVlTemperatura() {
        return vlTemperatura;
    }

    public void setVlTemperatura(BigDecimal vlTemperatura) {
        this.vlTemperatura = vlTemperatura;
    }

    public BigDecimal getVlVento() {
        return vlVento;
    }

    public void setVlVento(BigDecimal vlVento) {
        this.vlVento = vlVento;
    }

    public BigDecimal getVlPrecipitacao() {
        return vlPrecipitacao;
    }

    public void setVlPrecipitacao(BigDecimal vlPrecipitacao) {
        this.vlPrecipitacao = vlPrecipitacao;
    }

    public String getFgCondicaoTempo() {
        return fgCondicaoTempo;
    }

    public void setFgCondicaoTempo(String fgCondicaoTempo) {
        this.fgCondicaoTempo = fgCondicaoTempo;
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

    public String getFgRnc() {
        return fgRnc;
    }

    public void setFgRnc(String fgRnc) {
        this.fgRnc = fgRnc;
    }

    public String getNrRnc() {
        return nrRnc;
    }

    public void setNrRnc(String nrRnc) {
        this.nrRnc = nrRnc;
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

    public Integer getCdCalibracaoEquipamento() {
        return cdCalibracaoEquipamento;
    }

    public void setCdCalibracaoEquipamento(Integer cdCalibracaoEquipamento) {
        this.cdCalibracaoEquipamento = cdCalibracaoEquipamento;
    }
}