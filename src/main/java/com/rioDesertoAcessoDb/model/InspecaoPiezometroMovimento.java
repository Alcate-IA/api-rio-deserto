package com.rioDesertoAcessoDb.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class InspecaoPiezometroMovimento {

    private Integer cdInspecaoPiezometroMovto;
    private Integer cdInspecaoPiezometro;
    private LocalDate dtInspecao;
    private String nrMatriculaInspecao;
    private String nmColaboradorInspecao;
    private BigDecimal vlPrecipitacao;
    private BigDecimal qtLeitura;
    private BigDecimal qtNivelEstatico;
    private String fgCondicaoTempo;
    private String fgRnc;
    private String nrRnc;
    private String dsObservacao;
    private Integer cdUsuarioInclusao;
    private LocalDateTime dtInclusao;
    private Integer cdUsuarioAlteracao;
    private LocalDateTime dtAlteracao;
    private LocalDateTime dtImportacao;
    private Integer cdImportacao;
    private LocalDateTime dtEnvioEmail;
    private Integer cdCalibracaoEquipamento;
    private LocalDate dtAvaliacao;
    private String nrMatriculaAvaliacao;
    private String nmColaboradorAvaliacao;

    // Construtor padrão
    public InspecaoPiezometroMovimento() {
    }

    public Integer getCdInspecaoPiezometroMovto() {
        return cdInspecaoPiezometroMovto;
    }

    public void setCdInspecaoPiezometroMovto(Integer cdInspecaoPiezometroMovto) {
        this.cdInspecaoPiezometroMovto = cdInspecaoPiezometroMovto;
    }

    public Integer getCdInspecaoPiezometro() {
        return cdInspecaoPiezometro;
    }

    public void setCdInspecaoPiezometro(Integer cdInspecaoPiezometro) {
        this.cdInspecaoPiezometro = cdInspecaoPiezometro;
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

    public String getFgCondicaoTempo() {
        return fgCondicaoTempo;
    }

    public void setFgCondicaoTempo(String fgCondicaoTempo) {
        this.fgCondicaoTempo = fgCondicaoTempo;
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

    public LocalDateTime getDtImportacao() {
        return dtImportacao;
    }

    public void setDtImportacao(LocalDateTime dtImportacao) {
        this.dtImportacao = dtImportacao;
    }

    public Integer getCdImportacao() {
        return cdImportacao;
    }

    public void setCdImportacao(Integer cdImportacao) {
        this.cdImportacao = cdImportacao;
    }

    public LocalDateTime getDtEnvioEmail() {
        return dtEnvioEmail;
    }

    public void setDtEnvioEmail(LocalDateTime dtEnvioEmail) {
        this.dtEnvioEmail = dtEnvioEmail;
    }

    public Integer getCdCalibracaoEquipamento() {
        return cdCalibracaoEquipamento;
    }

    public void setCdCalibracaoEquipamento(Integer cdCalibracaoEquipamento) {
        this.cdCalibracaoEquipamento = cdCalibracaoEquipamento;
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