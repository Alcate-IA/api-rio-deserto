package com.rioDesertoAcessoDb.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InspecaoPiezometro {

    private Integer cdInspecaoPiezometro;
    private Integer cdEmpresa;
    private Integer cdPiezometro;
    private BigDecimal qtCotaBoca;
    private BigDecimal qtCotaSuperficie;
    private BigDecimal qtCotaBase;
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
    private BigDecimal vlNivelAtencao;
    private BigDecimal vlNivelAlerta;
    private BigDecimal vlNivelEmergencia;
    private String tpFrequencia;
    private BigDecimal vlNivelNormal;

    // Construtor padrão
    public InspecaoPiezometro() {
    }

    public Integer getCdInspecaoPiezometro() {
        return cdInspecaoPiezometro;
    }

    public void setCdInspecaoPiezometro(Integer cdInspecaoPiezometro) {
        this.cdInspecaoPiezometro = cdInspecaoPiezometro;
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

    public BigDecimal getQtCotaBoca() {
        return qtCotaBoca;
    }

    public void setQtCotaBoca(BigDecimal qtCotaBoca) {
        this.qtCotaBoca = qtCotaBoca;
    }

    public BigDecimal getQtCotaSuperficie() {
        return qtCotaSuperficie;
    }

    public void setQtCotaSuperficie(BigDecimal qtCotaSuperficie) {
        this.qtCotaSuperficie = qtCotaSuperficie;
    }

    public BigDecimal getQtCotaBase() {
        return qtCotaBase;
    }

    public void setQtCotaBase(BigDecimal qtCotaBase) {
        this.qtCotaBase = qtCotaBase;
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

    public BigDecimal getVlNivelAtencao() {
        return vlNivelAtencao;
    }

    public void setVlNivelAtencao(BigDecimal vlNivelAtencao) {
        this.vlNivelAtencao = vlNivelAtencao;
    }

    public BigDecimal getVlNivelAlerta() {
        return vlNivelAlerta;
    }

    public void setVlNivelAlerta(BigDecimal vlNivelAlerta) {
        this.vlNivelAlerta = vlNivelAlerta;
    }

    public BigDecimal getVlNivelEmergencia() {
        return vlNivelEmergencia;
    }

    public void setVlNivelEmergencia(BigDecimal vlNivelEmergencia) {
        this.vlNivelEmergencia = vlNivelEmergencia;
    }

    public String getTpFrequencia() {
        return tpFrequencia;
    }

    public void setTpFrequencia(String tpFrequencia) {
        this.tpFrequencia = tpFrequencia;
    }

    public BigDecimal getVlNivelNormal() {
        return vlNivelNormal;
    }

    public void setVlNivelNormal(BigDecimal vlNivelNormal) {
        this.vlNivelNormal = vlNivelNormal;
    }
}