package com.rioDesertoAcessoDb.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Empresa {

    private Integer cdPessoaEmpresa;
    private String fgSituacao;
    private String nrRegistroJuscesc;
    private LocalDate dtRegistroJuscesc;
    private Integer cdEmpresaPai;
    private String fgNfFatura;
    private String cdEmpresaAuxiliar;
    private Integer nrEmpresa;
    private String cdEmpresaOld;
    private BigDecimal pePisCr;
    private BigDecimal peCofinsCr;
    private String fgCreditaIcms;
    private String fgCreditaIpi;
    private BigDecimal pePisFaturamento;
    private BigDecimal peCofinsFaturamento;
    private BigDecimal peCfem;
    private Integer cdCentroCustoComercial;
    private Integer cdCentroCustoAlmoxarifado;
    private String fgIrf;
    private BigDecimal peIrf;
    private BigDecimal peCsll;
    private BigDecimal peEncargoFinanceiro;
    private Integer cdEmpresaSenior;
    private Integer cdFilialSenior;
    private Integer cdEmpresaVetorh;
    private Integer cdFilialVetorh;
    private String cdOrigemAlmoxarifado;
    private String cdOrigemProduto;
    private String cdOrigemServico;
    private Integer cdCentroCustoDvPrd;
    private Integer cdCentroCustoDvAdm;
    private Integer cdCentroCustoDvVen;
    private Integer cdContaCorrenteCaixa;
    private String tpEmpresa;
    private Integer cdContabilColigadaAtivo;
    private Integer cdContabilColigadaPassivo;
    private String nmEmpresaReduzido;
    private Integer cdUsuarioAutorizador;
    private Integer cdCentroCustoMp;
    private Integer cdCentroCustoMi;
    private Integer cdUsuarioResponsavel;
    private Integer cdContaCorrenteChqFolha;
    private String fgMensagemEv;
    private String nrCpfResponsavel;
    private Integer cdFilialCusto;
    private Integer cdContaCorrenteBoleto;
    private Integer qtColaboradorSubsolo;
    private Integer qtColaboradorSuperficie;
    private BigDecimal vlConstanteAltura;
    private BigDecimal vlConstanteLargura;
    private BigDecimal vlConstanteDensidade;
    private BigDecimal vlConstanteRafaProd;
    private BigDecimal qtAvancePrevisto;
    private String dsUnidadeProdutiva;
    private String tpImpressaoBoleto;
    private String fgFreteSt;
    private Integer cdUnidadeProdutiva;
    private Integer cdUsuarioInclusao;
    private LocalDateTime dtInclusao;
    private Integer cdUsuarioAlteracao;
    private LocalDateTime dtAlteracao;
    private String tpOrcamento;
    private BigDecimal peEncargoFolha;
    private Integer cdUsuarioRh;
    private Integer cdUsuarioTreinamento;
    private Integer cdUsuarioPessoal;
    private Integer cdDiretoria1;
    private Integer cdDiretoria2;
    private Integer cdUsuarioIa;
    private BigDecimal pePisCrMax;
    private BigDecimal peCofinsCrMax;
    private String nrRegistroMatControlado;
    private String nmSeguradora;
    private String nrApolice;
    private String nrAverbacao;
    private Integer cdUsuarioSeguranca;
    private Integer cdUsuarioMedicina;
    private Integer cdUsuarioSig;
    private Integer cdCentroCustoGps;
    private String sgOsmet;
    private Integer cdUsuarioDocumentacao;
    private String dsEmailEmpresa;
    private Integer cdAutorizadorOf;
    private Integer cdCentroCustoTransf;
    private Integer cdHcAc;
    private Integer cdHcBac;
    private Integer cdHcCp;
    private Integer cdHcAf;
    private Integer cdHcCr;
    private Integer cdHcCbx;
    private Integer cdHcCh;
    private Integer cdHcPc;
    private Integer cdHcDv;
    private Integer cdHcNfsDv;
    private Integer cdHcRm;
    private Integer cdHcRmDv;
    private Integer cdHcEc;
    private Integer cdHcNfs;
    private Integer cdHcNfe;
    private Integer cdHcRpa;
    private Integer cdHcTrb;
    private Integer cdHcTre;
    private String dsReciboLeite;
    private String nmFantasiaEmbarque;
    private String fgExigeOpProducao;
    private String tpComunicacaoBalanca;
    private String fgPesoEixo;
    private String modeloBalanca;
    private String portaComunicacao;
    private String baudRate;
    private String dataBits;
    private String parity;
    private String stopBits;
    private String handshaking;
    private Integer cdLocalCarregamento;
    private Integer cdLocalDescarregamento;
    private Integer cdCentroCustoManut;
    private Integer cdContabilColigadaFor;
    private Integer cdContabilColigadaCli;
    private Integer cdContabilColigadaCre;
    private Integer cdUsuarioPessoal2;
    private Integer cdContador;
    private Integer cdUsuarioPpp;
    private Integer cdEstruturaCargo;
    private String fgBalancaErro;
    private String tpPerfilEfi;
    private String tpAtividadeEfi;
    private Integer cdUsuarioPessoal3;
    private Integer cdUsuarioAvaliacao;
    private Integer cdClassificacaoTributaria;
    private String fgObrigaEcd;
    private String fgDesoneracaoFolha;
    private String fgIsencaoMulta;
    private LocalDate dtInicioReinf;
    private LocalDate dtFimReinf;
    private Integer cdUsuarioAlmoxarifado;
    private Integer cdUsuarioMedicina2;
    private Integer cdCentroCustoAi;
    private Integer cdUsuarioExpedicao;
    private Integer cdUsuarioLogistica;
    private String fgProducaoAprovaPedido;
    private Integer qtDiasPedido;
    private String fgEstoqueSolicitacao;
    private Integer cdUsuarioProducao;
    private String tpApuracaoDime;
    private Integer cdTributacaoIcmsDime;
    private String tpEscritaContabilDime;
    private Integer cdUsuarioTecnicoSeg;
    private String tpClassEstabInd;
    private String tpNaturezaEfp;
    private String tpAtividadeEfp;
    private LocalDate dtCartaoLeite;
    private String fgEstoqueEpi;
    private String nrRntrc;
    private Integer cdUsuarioLab;
    private String fgSaidaIsentaDif;
    private Integer cdFilialDox;
    private Integer cdUsuarioExposicaoRisco;
    private Integer cdMaterialLanche;
    private Integer nrDiasMaximoEstoque;
    private Integer cdUsuarioTreinamento2;

    // Construtor padrão
    public Empresa() {
    }

    // ========================
    // Getters e Setters
    // ========================

    public Integer getCdPessoaEmpresa() {
        return cdPessoaEmpresa;
    }

    public void setCdPessoaEmpresa(Integer cdPessoaEmpresa) {
        this.cdPessoaEmpresa = cdPessoaEmpresa;
    }

    public String getFgSituacao() {
        return fgSituacao;
    }

    public void setFgSituacao(String fgSituacao) {
        this.fgSituacao = fgSituacao;
    }

    public String getNrRegistroJuscesc() {
        return nrRegistroJuscesc;
    }

    public void setNrRegistroJuscesc(String nrRegistroJuscesc) {
        this.nrRegistroJuscesc = nrRegistroJuscesc;
    }

    public LocalDate getDtRegistroJuscesc() {
        return dtRegistroJuscesc;
    }

    public void setDtRegistroJuscesc(LocalDate dtRegistroJuscesc) {
        this.dtRegistroJuscesc = dtRegistroJuscesc;
    }

    public Integer getCdEmpresaPai() {
        return cdEmpresaPai;
    }

    public void setCdEmpresaPai(Integer cdEmpresaPai) {
        this.cdEmpresaPai = cdEmpresaPai;
    }

    public String getFgNfFatura() {
        return fgNfFatura;
    }

    public void setFgNfFatura(String fgNfFatura) {
        this.fgNfFatura = fgNfFatura;
    }

    public String getCdEmpresaAuxiliar() {
        return cdEmpresaAuxiliar;
    }

    public void setCdEmpresaAuxiliar(String cdEmpresaAuxiliar) {
        this.cdEmpresaAuxiliar = cdEmpresaAuxiliar;
    }

    public Integer getNrEmpresa() {
        return nrEmpresa;
    }

    public void setNrEmpresa(Integer nrEmpresa) {
        this.nrEmpresa = nrEmpresa;
    }

    public String getCdEmpresaOld() {
        return cdEmpresaOld;
    }

    public void setCdEmpresaOld(String cdEmpresaOld) {
        this.cdEmpresaOld = cdEmpresaOld;
    }

    public BigDecimal getPePisCr() {
        return pePisCr;
    }

    public void setPePisCr(BigDecimal pePisCr) {
        this.pePisCr = pePisCr;
    }

    public BigDecimal getPeCofinsCr() {
        return peCofinsCr;
    }

    public void setPeCofinsCr(BigDecimal peCofinsCr) {
        this.peCofinsCr = peCofinsCr;
    }

    public String getFgCreditaIcms() {
        return fgCreditaIcms;
    }

    public void setFgCreditaIcms(String fgCreditaIcms) {
        this.fgCreditaIcms = fgCreditaIcms;
    }

    public String getFgCreditaIpi() {
        return fgCreditaIpi;
    }

    public void setFgCreditaIpi(String fgCreditaIpi) {
        this.fgCreditaIpi = fgCreditaIpi;
    }

    public BigDecimal getPePisFaturamento() {
        return pePisFaturamento;
    }

    public void setPePisFaturamento(BigDecimal pePisFaturamento) {
        this.pePisFaturamento = pePisFaturamento;
    }

    public BigDecimal getPeCofinsFaturamento() {
        return peCofinsFaturamento;
    }

    public void setPeCofinsFaturamento(BigDecimal peCofinsFaturamento) {
        this.peCofinsFaturamento = peCofinsFaturamento;
    }

    public BigDecimal getPeCfem() {
        return peCfem;
    }

    public void setPeCfem(BigDecimal peCfem) {
        this.peCfem = peCfem;
    }

    public Integer getCdCentroCustoComercial() {
        return cdCentroCustoComercial;
    }

    public void setCdCentroCustoComercial(Integer cdCentroCustoComercial) {
        this.cdCentroCustoComercial = cdCentroCustoComercial;
    }

    public Integer getCdCentroCustoAlmoxarifado() {
        return cdCentroCustoAlmoxarifado;
    }

    public void setCdCentroCustoAlmoxarifado(Integer cdCentroCustoAlmoxarifado) {
        this.cdCentroCustoAlmoxarifado = cdCentroCustoAlmoxarifado;
    }

    public String getFgIrf() {
        return fgIrf;
    }

    public void setFgIrf(String fgIrf) {
        this.fgIrf = fgIrf;
    }

    public BigDecimal getPeIrf() {
        return peIrf;
    }

    public void setPeIrf(BigDecimal peIrf) {
        this.peIrf = peIrf;
    }

    public BigDecimal getPeCsll() {
        return peCsll;
    }

    public void setPeCsll(BigDecimal peCsll) {
        this.peCsll = peCsll;
    }

    public BigDecimal getPeEncargoFinanceiro() {
        return peEncargoFinanceiro;
    }

    public void setPeEncargoFinanceiro(BigDecimal peEncargoFinanceiro) {
        this.peEncargoFinanceiro = peEncargoFinanceiro;
    }

    public Integer getCdEmpresaSenior() {
        return cdEmpresaSenior;
    }

    public void setCdEmpresaSenior(Integer cdEmpresaSenior) {
        this.cdEmpresaSenior = cdEmpresaSenior;
    }

    public Integer getCdFilialSenior() {
        return cdFilialSenior;
    }

    public void setCdFilialSenior(Integer cdFilialSenior) {
        this.cdFilialSenior = cdFilialSenior;
    }

    public Integer getCdEmpresaVetorh() {
        return cdEmpresaVetorh;
    }

    public void setCdEmpresaVetorh(Integer cdEmpresaVetorh) {
        this.cdEmpresaVetorh = cdEmpresaVetorh;
    }

    public Integer getCdFilialVetorh() {
        return cdFilialVetorh;
    }

    public void setCdFilialVetorh(Integer cdFilialVetorh) {
        this.cdFilialVetorh = cdFilialVetorh;
    }

    public String getCdOrigemAlmoxarifado() {
        return cdOrigemAlmoxarifado;
    }

    public void setCdOrigemAlmoxarifado(String cdOrigemAlmoxarifado) {
        this.cdOrigemAlmoxarifado = cdOrigemAlmoxarifado;
    }

    public String getCdOrigemProduto() {
        return cdOrigemProduto;
    }

    public void setCdOrigemProduto(String cdOrigemProduto) {
        this.cdOrigemProduto = cdOrigemProduto;
    }

    public String getCdOrigemServico() {
        return cdOrigemServico;
    }

    public void setCdOrigemServico(String cdOrigemServico) {
        this.cdOrigemServico = cdOrigemServico;
    }

    public Integer getCdCentroCustoDvPrd() {
        return cdCentroCustoDvPrd;
    }

    public void setCdCentroCustoDvPrd(Integer cdCentroCustoDvPrd) {
        this.cdCentroCustoDvPrd = cdCentroCustoDvPrd;
    }

    public Integer getCdCentroCustoDvAdm() {
        return cdCentroCustoDvAdm;
    }

    public void setCdCentroCustoDvAdm(Integer cdCentroCustoDvAdm) {
        this.cdCentroCustoDvAdm = cdCentroCustoDvAdm;
    }

    public Integer getCdCentroCustoDvVen() {
        return cdCentroCustoDvVen;
    }

    public void setCdCentroCustoDvVen(Integer cdCentroCustoDvVen) {
        this.cdCentroCustoDvVen = cdCentroCustoDvVen;
    }

    public Integer getCdContaCorrenteCaixa() {
        return cdContaCorrenteCaixa;
    }

    public void setCdContaCorrenteCaixa(Integer cdContaCorrenteCaixa) {
        this.cdContaCorrenteCaixa = cdContaCorrenteCaixa;
    }

    public String getTpEmpresa() {
        return tpEmpresa;
    }

    public void setTpEmpresa(String tpEmpresa) {
        this.tpEmpresa = tpEmpresa;
    }

    public Integer getCdContabilColigadaAtivo() {
        return cdContabilColigadaAtivo;
    }

    public void setCdContabilColigadaAtivo(Integer cdContabilColigadaAtivo) {
        this.cdContabilColigadaAtivo = cdContabilColigadaAtivo;
    }

    public Integer getCdContabilColigadaPassivo() {
        return cdContabilColigadaPassivo;
    }

    public void setCdContabilColigadaPassivo(Integer cdContabilColigadaPassivo) {
        this.cdContabilColigadaPassivo = cdContabilColigadaPassivo;
    }

    public String getNmEmpresaReduzido() {
        return nmEmpresaReduzido;
    }

    public void setNmEmpresaReduzido(String nmEmpresaReduzido) {
        this.nmEmpresaReduzido = nmEmpresaReduzido;
    }

    public Integer getCdUsuarioAutorizador() {
        return cdUsuarioAutorizador;
    }

    public void setCdUsuarioAutorizador(Integer cdUsuarioAutorizador) {
        this.cdUsuarioAutorizador = cdUsuarioAutorizador;
    }

    public Integer getCdCentroCustoMp() {
        return cdCentroCustoMp;
    }

    public void setCdCentroCustoMp(Integer cdCentroCustoMp) {
        this.cdCentroCustoMp = cdCentroCustoMp;
    }

    public Integer getCdCentroCustoMi() {
        return cdCentroCustoMi;
    }

    public void setCdCentroCustoMi(Integer cdCentroCustoMi) {
        this.cdCentroCustoMi = cdCentroCustoMi;
    }

    public Integer getCdUsuarioResponsavel() {
        return cdUsuarioResponsavel;
    }

    public void setCdUsuarioResponsavel(Integer cdUsuarioResponsavel) {
        this.cdUsuarioResponsavel = cdUsuarioResponsavel;
    }

    public Integer getCdContaCorrenteChqFolha() {
        return cdContaCorrenteChqFolha;
    }

    public void setCdContaCorrenteChqFolha(Integer cdContaCorrenteChqFolha) {
        this.cdContaCorrenteChqFolha = cdContaCorrenteChqFolha;
    }

    public String getFgMensagemEv() {
        return fgMensagemEv;
    }

    public void setFgMensagemEv(String fgMensagemEv) {
        this.fgMensagemEv = fgMensagemEv;
    }

    public String getNrCpfResponsavel() {
        return nrCpfResponsavel;
    }

    public void setNrCpfResponsavel(String nrCpfResponsavel) {
        this.nrCpfResponsavel = nrCpfResponsavel;
    }

    public Integer getCdFilialCusto() {
        return cdFilialCusto;
    }

    public void setCdFilialCusto(Integer cdFilialCusto) {
        this.cdFilialCusto = cdFilialCusto;
    }

    public Integer getCdContaCorrenteBoleto() {
        return cdContaCorrenteBoleto;
    }

    public void setCdContaCorrenteBoleto(Integer cdContaCorrenteBoleto) {
        this.cdContaCorrenteBoleto = cdContaCorrenteBoleto;
    }

    public Integer getQtColaboradorSubsolo() {
        return qtColaboradorSubsolo;
    }

    public void setQtColaboradorSubsolo(Integer qtColaboradorSubsolo) {
        this.qtColaboradorSubsolo = qtColaboradorSubsolo;
    }

    public Integer getQtColaboradorSuperficie() {
        return qtColaboradorSuperficie;
    }

    public void setQtColaboradorSuperficie(Integer qtColaboradorSuperficie) {
        this.qtColaboradorSuperficie = qtColaboradorSuperficie;
    }

    public BigDecimal getVlConstanteAltura() {
        return vlConstanteAltura;
    }

    public void setVlConstanteAltura(BigDecimal vlConstanteAltura) {
        this.vlConstanteAltura = vlConstanteAltura;
    }

    public BigDecimal getVlConstanteLargura() {
        return vlConstanteLargura;
    }

    public void setVlConstanteLargura(BigDecimal vlConstanteLargura) {
        this.vlConstanteLargura = vlConstanteLargura;
    }

    public BigDecimal getVlConstanteDensidade() {
        return vlConstanteDensidade;
    }

    public void setVlConstanteDensidade(BigDecimal vlConstanteDensidade) {
        this.vlConstanteDensidade = vlConstanteDensidade;
    }

    public BigDecimal getVlConstanteRafaProd() {
        return vlConstanteRafaProd;
    }

    public void setVlConstanteRafaProd(BigDecimal vlConstanteRafaProd) {
        this.vlConstanteRafaProd = vlConstanteRafaProd;
    }

    public BigDecimal getQtAvancePrevisto() {
        return qtAvancePrevisto;
    }

    public void setQtAvancePrevisto(BigDecimal qtAvancePrevisto) {
        this.qtAvancePrevisto = qtAvancePrevisto;
    }

    public String getDsUnidadeProdutiva() {
        return dsUnidadeProdutiva;
    }

    public void setDsUnidadeProdutiva(String dsUnidadeProdutiva) {
        this.dsUnidadeProdutiva = dsUnidadeProdutiva;
    }

    public String getTpImpressaoBoleto() {
        return tpImpressaoBoleto;
    }

    public void setTpImpressaoBoleto(String tpImpressaoBoleto) {
        this.tpImpressaoBoleto = tpImpressaoBoleto;
    }

    public String getFgFreteSt() {
        return fgFreteSt;
    }

    public void setFgFreteSt(String fgFreteSt) {
        this.fgFreteSt = fgFreteSt;
    }

    public Integer getCdUnidadeProdutiva() {
        return cdUnidadeProdutiva;
    }

    public void setCdUnidadeProdutiva(Integer cdUnidadeProdutiva) {
        this.cdUnidadeProdutiva = cdUnidadeProdutiva;
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

    public String getTpOrcamento() {
        return tpOrcamento;
    }

    public void setTpOrcamento(String tpOrcamento) {
        this.tpOrcamento = tpOrcamento;
    }

    public BigDecimal getPeEncargoFolha() {
        return peEncargoFolha;
    }

    public void setPeEncargoFolha(BigDecimal peEncargoFolha) {
        this.peEncargoFolha = peEncargoFolha;
    }

    public Integer getCdUsuarioRh() {
        return cdUsuarioRh;
    }

    public void setCdUsuarioRh(Integer cdUsuarioRh) {
        this.cdUsuarioRh = cdUsuarioRh;
    }

    public Integer getCdUsuarioTreinamento() {
        return cdUsuarioTreinamento;
    }

    public void setCdUsuarioTreinamento(Integer cdUsuarioTreinamento) {
        this.cdUsuarioTreinamento = cdUsuarioTreinamento;
    }

    public Integer getCdUsuarioPessoal() {
        return cdUsuarioPessoal;
    }

    public void setCdUsuarioPessoal(Integer cdUsuarioPessoal) {
        this.cdUsuarioPessoal = cdUsuarioPessoal;
    }

    public Integer getCdDiretoria1() {
        return cdDiretoria1;
    }

    public void setCdDiretoria1(Integer cdDiretoria1) {
        this.cdDiretoria1 = cdDiretoria1;
    }

    public Integer getCdDiretoria2() {
        return cdDiretoria2;
    }

    public void setCdDiretoria2(Integer cdDiretoria2) {
        this.cdDiretoria2 = cdDiretoria2;
    }

    public Integer getCdUsuarioIa() {
        return cdUsuarioIa;
    }

    public void setCdUsuarioIa(Integer cdUsuarioIa) {
        this.cdUsuarioIa = cdUsuarioIa;
    }

    public BigDecimal getPePisCrMax() {
        return pePisCrMax;
    }

    public void setPePisCrMax(BigDecimal pePisCrMax) {
        this.pePisCrMax = pePisCrMax;
    }

    public BigDecimal getPeCofinsCrMax() {
        return peCofinsCrMax;
    }

    public void setPeCofinsCrMax(BigDecimal peCofinsCrMax) {
        this.peCofinsCrMax = peCofinsCrMax;
    }

    public String getNrRegistroMatControlado() {
        return nrRegistroMatControlado;
    }

    public void setNrRegistroMatControlado(String nrRegistroMatControlado) {
        this.nrRegistroMatControlado = nrRegistroMatControlado;
    }

    public String getNmSeguradora() {
        return nmSeguradora;
    }

    public void setNmSeguradora(String nmSeguradora) {
        this.nmSeguradora = nmSeguradora;
    }

    public String getNrApolice() {
        return nrApolice;
    }

    public void setNrApolice(String nrApolice) {
        this.nrApolice = nrApolice;
    }

    public String getNrAverbacao() {
        return nrAverbacao;
    }

    public void setNrAverbacao(String nrAverbacao) {
        this.nrAverbacao = nrAverbacao;
    }

    public Integer getCdUsuarioSeguranca() {
        return cdUsuarioSeguranca;
    }

    public void setCdUsuarioSeguranca(Integer cdUsuarioSeguranca) {
        this.cdUsuarioSeguranca = cdUsuarioSeguranca;
    }

    public Integer getCdUsuarioMedicina() {
        return cdUsuarioMedicina;
    }

    public void setCdUsuarioMedicina(Integer cdUsuarioMedicina) {
        this.cdUsuarioMedicina = cdUsuarioMedicina;
    }

    public Integer getCdUsuarioSig() {
        return cdUsuarioSig;
    }

    public void setCdUsuarioSig(Integer cdUsuarioSig) {
        this.cdUsuarioSig = cdUsuarioSig;
    }

    public Integer getCdCentroCustoGps() {
        return cdCentroCustoGps;
    }

    public void setCdCentroCustoGps(Integer cdCentroCustoGps) {
        this.cdCentroCustoGps = cdCentroCustoGps;
    }

    public String getSgOsmet() {
        return sgOsmet;
    }

    public void setSgOsmet(String sgOsmet) {
        this.sgOsmet = sgOsmet;
    }

    public Integer getCdUsuarioDocumentacao() {
        return cdUsuarioDocumentacao;
    }

    public void setCdUsuarioDocumentacao(Integer cdUsuarioDocumentacao) {
        this.cdUsuarioDocumentacao = cdUsuarioDocumentacao;
    }

    public String getDsEmailEmpresa() {
        return dsEmailEmpresa;
    }

    public void setDsEmailEmpresa(String dsEmailEmpresa) {
        this.dsEmailEmpresa = dsEmailEmpresa;
    }

    public Integer getCdAutorizadorOf() {
        return cdAutorizadorOf;
    }

    public void setCdAutorizadorOf(Integer cdAutorizadorOf) {
        this.cdAutorizadorOf = cdAutorizadorOf;
    }

    public Integer getCdCentroCustoTransf() {
        return cdCentroCustoTransf;
    }

    public void setCdCentroCustoTransf(Integer cdCentroCustoTransf) {
        this.cdCentroCustoTransf = cdCentroCustoTransf;
    }

    public Integer getCdHcAc() {
        return cdHcAc;
    }

    public void setCdHcAc(Integer cdHcAc) {
        this.cdHcAc = cdHcAc;
    }

    public Integer getCdHcBac() {
        return cdHcBac;
    }

    public void setCdHcBac(Integer cdHcBac) {
        this.cdHcBac = cdHcBac;
    }

    public Integer getCdHcCp() {
        return cdHcCp;
    }

    public void setCdHcCp(Integer cdHcCp) {
        this.cdHcCp = cdHcCp;
    }

    public Integer getCdHcAf() {
        return cdHcAf;
    }

    public void setCdHcAf(Integer cdHcAf) {
        this.cdHcAf = cdHcAf;
    }

    public Integer getCdHcCr() {
        return cdHcCr;
    }

    public void setCdHcCr(Integer cdHcCr) {
        this.cdHcCr = cdHcCr;
    }

    public Integer getCdHcCbx() {
        return cdHcCbx;
    }

    public void setCdHcCbx(Integer cdHcCbx) {
        this.cdHcCbx = cdHcCbx;
    }

    public Integer getCdHcCh() {
        return cdHcCh;
    }

    public void setCdHcCh(Integer cdHcCh) {
        this.cdHcCh = cdHcCh;
    }

    public Integer getCdHcPc() {
        return cdHcPc;
    }

    public void setCdHcPc(Integer cdHcPc) {
        this.cdHcPc = cdHcPc;
    }

    public Integer getCdHcDv() {
        return cdHcDv;
    }

    public void setCdHcDv(Integer cdHcDv) {
        this.cdHcDv = cdHcDv;
    }

    public Integer getCdHcNfsDv() {
        return cdHcNfsDv;
    }

    public void setCdHcNfsDv(Integer cdHcNfsDv) {
        this.cdHcNfsDv = cdHcNfsDv;
    }

    public Integer getCdHcRm() {
        return cdHcRm;
    }

    public void setCdHcRm(Integer cdHcRm) {
        this.cdHcRm = cdHcRm;
    }

    public Integer getCdHcRmDv() {
        return cdHcRmDv;
    }

    public void setCdHcRmDv(Integer cdHcRmDv) {
        this.cdHcRmDv = cdHcRmDv;
    }

    public Integer getCdHcEc() {
        return cdHcEc;
    }

    public void setCdHcEc(Integer cdHcEc) {
        this.cdHcEc = cdHcEc;
    }

    public Integer getCdHcNfs() {
        return cdHcNfs;
    }

    public void setCdHcNfs(Integer cdHcNfs) {
        this.cdHcNfs = cdHcNfs;
    }

    public Integer getCdHcNfe() {
        return cdHcNfe;
    }

    public void setCdHcNfe(Integer cdHcNfe) {
        this.cdHcNfe = cdHcNfe;
    }

    public Integer getCdHcRpa() {
        return cdHcRpa;
    }

    public void setCdHcRpa(Integer cdHcRpa) {
        this.cdHcRpa = cdHcRpa;
    }

    public Integer getCdHcTrb() {
        return cdHcTrb;
    }

    public void setCdHcTrb(Integer cdHcTrb) {
        this.cdHcTrb = cdHcTrb;
    }

    public Integer getCdHcTre() {
        return cdHcTre;
    }

    public void setCdHcTre(Integer cdHcTre) {
        this.cdHcTre = cdHcTre;
    }

    public String getDsReciboLeite() {
        return dsReciboLeite;
    }

    public void setDsReciboLeite(String dsReciboLeite) {
        this.dsReciboLeite = dsReciboLeite;
    }

    public String getNmFantasiaEmbarque() {
        return nmFantasiaEmbarque;
    }

    public void setNmFantasiaEmbarque(String nmFantasiaEmbarque) {
        this.nmFantasiaEmbarque = nmFantasiaEmbarque;
    }

    public String getFgExigeOpProducao() {
        return fgExigeOpProducao;
    }

    public void setFgExigeOpProducao(String fgExigeOpProducao) {
        this.fgExigeOpProducao = fgExigeOpProducao;
    }

    public String getTpComunicacaoBalanca() {
        return tpComunicacaoBalanca;
    }

    public void setTpComunicacaoBalanca(String tpComunicacaoBalanca) {
        this.tpComunicacaoBalanca = tpComunicacaoBalanca;
    }

    public String getFgPesoEixo() {
        return fgPesoEixo;
    }

    public void setFgPesoEixo(String fgPesoEixo) {
        this.fgPesoEixo = fgPesoEixo;
    }

    public String getModeloBalanca() {
        return modeloBalanca;
    }

    public void setModeloBalanca(String modeloBalanca) {
        this.modeloBalanca = modeloBalanca;
    }

    public String getPortaComunicacao() {
        return portaComunicacao;
    }

    public void setPortaComunicacao(String portaComunicacao) {
        this.portaComunicacao = portaComunicacao;
    }

    public String getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(String baudRate) {
        this.baudRate = baudRate;
    }

    public String getDataBits() {
        return dataBits;
    }

    public void setDataBits(String dataBits) {
        this.dataBits = dataBits;
    }

    public String getParity() {
        return parity;
    }

    public void setParity(String parity) {
        this.parity = parity;
    }

    public String getStopBits() {
        return stopBits;
    }

    public void setStopBits(String stopBits) {
        this.stopBits = stopBits;
    }

    public String getHandshaking() {
        return handshaking;
    }

    public void setHandshaking(String handshaking) {
        this.handshaking = handshaking;
    }

    public Integer getCdLocalCarregamento() {
        return cdLocalCarregamento;
    }

    public void setCdLocalCarregamento(Integer cdLocalCarregamento) {
        this.cdLocalCarregamento = cdLocalCarregamento;
    }

    public Integer getCdLocalDescarregamento() {
        return cdLocalDescarregamento;
    }

    public void setCdLocalDescarregamento(Integer cdLocalDescarregamento) {
        this.cdLocalDescarregamento = cdLocalDescarregamento;
    }

    public Integer getCdCentroCustoManut() {
        return cdCentroCustoManut;
    }

    public void setCdCentroCustoManut(Integer cdCentroCustoManut) {
        this.cdCentroCustoManut = cdCentroCustoManut;
    }

    public Integer getCdContabilColigadaFor() {
        return cdContabilColigadaFor;
    }

    public void setCdContabilColigadaFor(Integer cdContabilColigadaFor) {
        this.cdContabilColigadaFor = cdContabilColigadaFor;
    }

    public Integer getCdContabilColigadaCli() {
        return cdContabilColigadaCli;
    }

    public void setCdContabilColigadaCli(Integer cdContabilColigadaCli) {
        this.cdContabilColigadaCli = cdContabilColigadaCli;
    }

    public Integer getCdContabilColigadaCre() {
        return cdContabilColigadaCre;
    }

    public void setCdContabilColigadaCre(Integer cdContabilColigadaCre) {
        this.cdContabilColigadaCre = cdContabilColigadaCre;
    }

    public Integer getCdUsuarioPessoal2() {
        return cdUsuarioPessoal2;
    }

    public void setCdUsuarioPessoal2(Integer cdUsuarioPessoal2) {
        this.cdUsuarioPessoal2 = cdUsuarioPessoal2;
    }

    public Integer getCdContador() {
        return cdContador;
    }

    public void setCdContador(Integer cdContador) {
        this.cdContador = cdContador;
    }

    public Integer getCdUsuarioPpp() {
        return cdUsuarioPpp;
    }

    public void setCdUsuarioPpp(Integer cdUsuarioPpp) {
        this.cdUsuarioPpp = cdUsuarioPpp;
    }

    public Integer getCdEstruturaCargo() {
        return cdEstruturaCargo;
    }

    public void setCdEstruturaCargo(Integer cdEstruturaCargo) {
        this.cdEstruturaCargo = cdEstruturaCargo;
    }

    public String getFgBalancaErro() {
        return fgBalancaErro;
    }

    public void setFgBalancaErro(String fgBalancaErro) {
        this.fgBalancaErro = fgBalancaErro;
    }

    public String getTpPerfilEfi() {
        return tpPerfilEfi;
    }

    public void setTpPerfilEfi(String tpPerfilEfi) {
        this.tpPerfilEfi = tpPerfilEfi;
    }

    public String getTpAtividadeEfi() {
        return tpAtividadeEfi;
    }

    public void setTpAtividadeEfi(String tpAtividadeEfi) {
        this.tpAtividadeEfi = tpAtividadeEfi;
    }

    public Integer getCdUsuarioPessoal3() {
        return cdUsuarioPessoal3;
    }

    public void setCdUsuarioPessoal3(Integer cdUsuarioPessoal3) {
        this.cdUsuarioPessoal3 = cdUsuarioPessoal3;
    }

    public Integer getCdUsuarioAvaliacao() {
        return cdUsuarioAvaliacao;
    }

    public void setCdUsuarioAvaliacao(Integer cdUsuarioAvaliacao) {
        this.cdUsuarioAvaliacao = cdUsuarioAvaliacao;
    }

    public Integer getCdClassificacaoTributaria() {
        return cdClassificacaoTributaria;
    }

    public void setCdClassificacaoTributaria(Integer cdClassificacaoTributaria) {
        this.cdClassificacaoTributaria = cdClassificacaoTributaria;
    }

    public String getFgObrigaEcd() {
        return fgObrigaEcd;
    }

    public void setFgObrigaEcd(String fgObrigaEcd) {
        this.fgObrigaEcd = fgObrigaEcd;
    }

    public String getFgDesoneracaoFolha() {
        return fgDesoneracaoFolha;
    }

    public void setFgDesoneracaoFolha(String fgDesoneracaoFolha) {
        this.fgDesoneracaoFolha = fgDesoneracaoFolha;
    }

    public String getFgIsencaoMulta() {
        return fgIsencaoMulta;
    }

    public void setFgIsencaoMulta(String fgIsencaoMulta) {
        this.fgIsencaoMulta = fgIsencaoMulta;
    }

    public LocalDate getDtInicioReinf() {
        return dtInicioReinf;
    }

    public void setDtInicioReinf(LocalDate dtInicioReinf) {
        this.dtInicioReinf = dtInicioReinf;
    }

    public LocalDate getDtFimReinf() {
        return dtFimReinf;
    }

    public void setDtFimReinf(LocalDate dtFimReinf) {
        this.dtFimReinf = dtFimReinf;
    }

    public Integer getCdUsuarioAlmoxarifado() {
        return cdUsuarioAlmoxarifado;
    }

    public void setCdUsuarioAlmoxarifado(Integer cdUsuarioAlmoxarifado) {
        this.cdUsuarioAlmoxarifado = cdUsuarioAlmoxarifado;
    }

    public Integer getCdUsuarioMedicina2() {
        return cdUsuarioMedicina2;
    }

    public void setCdUsuarioMedicina2(Integer cdUsuarioMedicina2) {
        this.cdUsuarioMedicina2 = cdUsuarioMedicina2;
    }

    public Integer getCdCentroCustoAi() {
        return cdCentroCustoAi;
    }

    public void setCdCentroCustoAi(Integer cdCentroCustoAi) {
        this.cdCentroCustoAi = cdCentroCustoAi;
    }

    public Integer getCdUsuarioExpedicao() {
        return cdUsuarioExpedicao;
    }

    public void setCdUsuarioExpedicao(Integer cdUsuarioExpedicao) {
        this.cdUsuarioExpedicao = cdUsuarioExpedicao;
    }

    public Integer getCdUsuarioLogistica() {
        return cdUsuarioLogistica;
    }

    public void setCdUsuarioLogistica(Integer cdUsuarioLogistica) {
        this.cdUsuarioLogistica = cdUsuarioLogistica;
    }

    public String getFgProducaoAprovaPedido() {
        return fgProducaoAprovaPedido;
    }

    public void setFgProducaoAprovaPedido(String fgProducaoAprovaPedido) {
        this.fgProducaoAprovaPedido = fgProducaoAprovaPedido;
    }

    public Integer getQtDiasPedido() {
        return qtDiasPedido;
    }

    public void setQtDiasPedido(Integer qtDiasPedido) {
        this.qtDiasPedido = qtDiasPedido;
    }

    public String getFgEstoqueSolicitacao() {
        return fgEstoqueSolicitacao;
    }

    public void setFgEstoqueSolicitacao(String fgEstoqueSolicitacao) {
        this.fgEstoqueSolicitacao = fgEstoqueSolicitacao;
    }

    public Integer getCdUsuarioProducao() {
        return cdUsuarioProducao;
    }

    public void setCdUsuarioProducao(Integer cdUsuarioProducao) {
        this.cdUsuarioProducao = cdUsuarioProducao;
    }

    public String getTpApuracaoDime() {
        return tpApuracaoDime;
    }

    public void setTpApuracaoDime(String tpApuracaoDime) {
        this.tpApuracaoDime = tpApuracaoDime;
    }

    public Integer getCdTributacaoIcmsDime() {
        return cdTributacaoIcmsDime;
    }

    public void setCdTributacaoIcmsDime(Integer cdTributacaoIcmsDime) {
        this.cdTributacaoIcmsDime = cdTributacaoIcmsDime;
    }

    public String getTpEscritaContabilDime() {
        return tpEscritaContabilDime;
    }

    public void setTpEscritaContabilDime(String tpEscritaContabilDime) {
        this.tpEscritaContabilDime = tpEscritaContabilDime;
    }

    public Integer getCdUsuarioTecnicoSeg() {
        return cdUsuarioTecnicoSeg;
    }

    public void setCdUsuarioTecnicoSeg(Integer cdUsuarioTecnicoSeg) {
        this.cdUsuarioTecnicoSeg = cdUsuarioTecnicoSeg;
    }

    public String getTpClassEstabInd() {
        return tpClassEstabInd;
    }

    public void setTpClassEstabInd(String tpClassEstabInd) {
        this.tpClassEstabInd = tpClassEstabInd;
    }

    public String getTpNaturezaEfp() {
        return tpNaturezaEfp;
    }

    public void setTpNaturezaEfp(String tpNaturezaEfp) {
        this.tpNaturezaEfp = tpNaturezaEfp;
    }

    public String getTpAtividadeEfp() {
        return tpAtividadeEfp;
    }

    public void setTpAtividadeEfp(String tpAtividadeEfp) {
        this.tpAtividadeEfp = tpAtividadeEfp;
    }

    public LocalDate getDtCartaoLeite() {
        return dtCartaoLeite;
    }

    public void setDtCartaoLeite(LocalDate dtCartaoLeite) {
        this.dtCartaoLeite = dtCartaoLeite;
    }

    public String getFgEstoqueEpi() {
        return fgEstoqueEpi;
    }

    public void setFgEstoqueEpi(String fgEstoqueEpi) {
        this.fgEstoqueEpi = fgEstoqueEpi;
    }

    public String getNrRntrc() {
        return nrRntrc;
    }

    public void setNrRntrc(String nrRntrc) {
        this.nrRntrc = nrRntrc;
    }

    public Integer getCdUsuarioLab() {
        return cdUsuarioLab;
    }

    public void setCdUsuarioLab(Integer cdUsuarioLab) {
        this.cdUsuarioLab = cdUsuarioLab;
    }

    public String getFgSaidaIsentaDif() {
        return fgSaidaIsentaDif;
    }

    public void setFgSaidaIsentaDif(String fgSaidaIsentaDif) {
        this.fgSaidaIsentaDif = fgSaidaIsentaDif;
    }

    public Integer getCdFilialDox() {
        return cdFilialDox;
    }

    public void setCdFilialDox(Integer cdFilialDox) {
        this.cdFilialDox = cdFilialDox;
    }

    public Integer getCdUsuarioExposicaoRisco() {
        return cdUsuarioExposicaoRisco;
    }

    public void setCdUsuarioExposicaoRisco(Integer cdUsuarioExposicaoRisco) {
        this.cdUsuarioExposicaoRisco = cdUsuarioExposicaoRisco;
    }

    public Integer getCdMaterialLanche() {
        return cdMaterialLanche;
    }

    public void setCdMaterialLanche(Integer cdMaterialLanche) {
        this.cdMaterialLanche = cdMaterialLanche;
    }

    public Integer getNrDiasMaximoEstoque() {
        return nrDiasMaximoEstoque;
    }

    public void setNrDiasMaximoEstoque(Integer nrDiasMaximoEstoque) {
        this.nrDiasMaximoEstoque = nrDiasMaximoEstoque;
    }

    public Integer getCdUsuarioTreinamento2() {
        return cdUsuarioTreinamento2;
    }

    public void setCdUsuarioTreinamento2(Integer cdUsuarioTreinamento2) {
        this.cdUsuarioTreinamento2 = cdUsuarioTreinamento2;
    }
}