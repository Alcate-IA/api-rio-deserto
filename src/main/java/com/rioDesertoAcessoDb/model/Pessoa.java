package com.rioDesertoAcessoDb.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Pessoa {
    private Integer cdPessoa;
    private String fgFisicaJuridica;
    private String nmPessoa;
    private String nmFantasia;
    private String nrCpf;
    private String nrRg;
    private String nrCnpj;
    private String nrIe;
    private String nrIm;
    private String dsEndereco;
    private String dsComplemento;
    private String nmBairro;
    private String nrCep;
    private Integer cdCidade;
    private String dsEnderecoFat;
    private String dsComplementoFat;
    private String nmBairroFat;
    private String nrCepFat;
    private Integer cdCidadeFat;
    private String nrInscricaoInss;
    private String fgRetemIss;
    private String fgOptanteSimples;
    private String fgIsentoIcms;
    private String fgIncideFunrural;
    private String fgIncideIcmsSt;
    private String fgCooperativa;
    private Integer cdUsuarioInclusao;
    private Integer cdUsuarioAlteracao;
    private LocalDateTime dtInclusao;
    private LocalDateTime dtAlteracao;
    private Integer cdPessoaOld;
    private Integer cdEmpresaFilialOld;
    private LocalDateTime dtIntegracaoCliente;
    private LocalDateTime dtIntegracaoFornecedor;
    private String fgGenerica;
    private String fgEmpresaColigada;
    private String dsSenhaB2b;
    private String fgDescontoTractebel;
    private String cdSuframa;
    private LocalDate dtNascimento;
    private String poLatitude;
    private String poLongitude;
    private Integer cdFormaPagamentoTp;
    private Integer cdFormaPagamentoTr;
    private Integer cdCondicaoPagamentoCp;
    private String nrCrm;
    private String fgRevisado;
    private LocalDateTime dtUltimaRevisao;
    private Integer cdUsuarioRevisao;
    private String fgProdutorRural;
    private String fgOptanteMei;
    private String dsSenhaCliente;
    private String fgSituacao;
    private String fgIssSubstituicao;

    public Integer getCdPessoa() {
        return cdPessoa;
    }

    public void setCdPessoa(Integer cdPessoa) {
        this.cdPessoa = cdPessoa;
    }

    public String getFgFisicaJuridica() {
        return fgFisicaJuridica;
    }

    public void setFgFisicaJuridica(String fgFisicaJuridica) {
        this.fgFisicaJuridica = fgFisicaJuridica;
    }

    public String getNmPessoa() {
        return nmPessoa;
    }

    public void setNmPessoa(String nmPessoa) {
        this.nmPessoa = nmPessoa;
    }

    public String getNmFantasia() {
        return nmFantasia;
    }

    public void setNmFantasia(String nmFantasia) {
        this.nmFantasia = nmFantasia;
    }

    public String getNrCpf() {
        return nrCpf;
    }

    public void setNrCpf(String nrCpf) {
        this.nrCpf = nrCpf;
    }

    public String getNrRg() {
        return nrRg;
    }

    public void setNrRg(String nrRg) {
        this.nrRg = nrRg;
    }

    public String getNrCnpj() {
        return nrCnpj;
    }

    public void setNrCnpj(String nrCnpj) {
        this.nrCnpj = nrCnpj;
    }

    public String getNrIe() {
        return nrIe;
    }

    public void setNrIe(String nrIe) {
        this.nrIe = nrIe;
    }

    public String getNrIm() {
        return nrIm;
    }

    public void setNrIm(String nrIm) {
        this.nrIm = nrIm;
    }

    public String getDsEndereco() {
        return dsEndereco;
    }

    public void setDsEndereco(String dsEndereco) {
        this.dsEndereco = dsEndereco;
    }

    public String getDsComplemento() {
        return dsComplemento;
    }

    public void setDsComplemento(String dsComplemento) {
        this.dsComplemento = dsComplemento;
    }

    public String getNmBairro() {
        return nmBairro;
    }

    public void setNmBairro(String nmBairro) {
        this.nmBairro = nmBairro;
    }

    public String getNrCep() {
        return nrCep;
    }

    public void setNrCep(String nrCep) {
        this.nrCep = nrCep;
    }

    public Integer getCdCidade() {
        return cdCidade;
    }

    public void setCdCidade(Integer cdCidade) {
        this.cdCidade = cdCidade;
    }

    public String getDsEnderecoFat() {
        return dsEnderecoFat;
    }

    public void setDsEnderecoFat(String dsEnderecoFat) {
        this.dsEnderecoFat = dsEnderecoFat;
    }

    public String getDsComplementoFat() {
        return dsComplementoFat;
    }

    public void setDsComplementoFat(String dsComplementoFat) {
        this.dsComplementoFat = dsComplementoFat;
    }

    public String getNmBairroFat() {
        return nmBairroFat;
    }

    public void setNmBairroFat(String nmBairroFat) {
        this.nmBairroFat = nmBairroFat;
    }

    public String getNrCepFat() {
        return nrCepFat;
    }

    public void setNrCepFat(String nrCepFat) {
        this.nrCepFat = nrCepFat;
    }

    public Integer getCdCidadeFat() {
        return cdCidadeFat;
    }

    public void setCdCidadeFat(Integer cdCidadeFat) {
        this.cdCidadeFat = cdCidadeFat;
    }

    public String getNrInscricaoInss() {
        return nrInscricaoInss;
    }

    public void setNrInscricaoInss(String nrInscricaoInss) {
        this.nrInscricaoInss = nrInscricaoInss;
    }

    public String getFgRetemIss() {
        return fgRetemIss;
    }

    public void setFgRetemIss(String fgRetemIss) {
        this.fgRetemIss = fgRetemIss;
    }

    public String getFgOptanteSimples() {
        return fgOptanteSimples;
    }

    public void setFgOptanteSimples(String fgOptanteSimples) {
        this.fgOptanteSimples = fgOptanteSimples;
    }

    public String getFgIsentoIcms() {
        return fgIsentoIcms;
    }

    public void setFgIsentoIcms(String fgIsentoIcms) {
        this.fgIsentoIcms = fgIsentoIcms;
    }

    public String getFgIncideFunrural() {
        return fgIncideFunrural;
    }

    public void setFgIncideFunrural(String fgIncideFunrural) {
        this.fgIncideFunrural = fgIncideFunrural;
    }

    public String getFgIncideIcmsSt() {
        return fgIncideIcmsSt;
    }

    public void setFgIncideIcmsSt(String fgIncideIcmsSt) {
        this.fgIncideIcmsSt = fgIncideIcmsSt;
    }

    public String getFgCooperativa() {
        return fgCooperativa;
    }

    public void setFgCooperativa(String fgCooperativa) {
        this.fgCooperativa = fgCooperativa;
    }

    public Integer getCdUsuarioInclusao() {
        return cdUsuarioInclusao;
    }

    public void setCdUsuarioInclusao(Integer cdUsuarioInclusao) {
        this.cdUsuarioInclusao = cdUsuarioInclusao;
    }

    public Integer getCdUsuarioAlteracao() {
        return cdUsuarioAlteracao;
    }

    public void setCdUsuarioAlteracao(Integer cdUsuarioAlteracao) {
        this.cdUsuarioAlteracao = cdUsuarioAlteracao;
    }

    public LocalDateTime getDtInclusao() {
        return dtInclusao;
    }

    public void setDtInclusao(LocalDateTime dtInclusao) {
        this.dtInclusao = dtInclusao;
    }

    public LocalDateTime getDtAlteracao() {
        return dtAlteracao;
    }

    public void setDtAlteracao(LocalDateTime dtAlteracao) {
        this.dtAlteracao = dtAlteracao;
    }

    public Integer getCdPessoaOld() {
        return cdPessoaOld;
    }

    public void setCdPessoaOld(Integer cdPessoaOld) {
        this.cdPessoaOld = cdPessoaOld;
    }

    public Integer getCdEmpresaFilialOld() {
        return cdEmpresaFilialOld;
    }

    public void setCdEmpresaFilialOld(Integer cdEmpresaFilialOld) {
        this.cdEmpresaFilialOld = cdEmpresaFilialOld;
    }

    public LocalDateTime getDtIntegracaoCliente() {
        return dtIntegracaoCliente;
    }

    public void setDtIntegracaoCliente(LocalDateTime dtIntegracaoCliente) {
        this.dtIntegracaoCliente = dtIntegracaoCliente;
    }

    public LocalDateTime getDtIntegracaoFornecedor() {
        return dtIntegracaoFornecedor;
    }

    public void setDtIntegracaoFornecedor(LocalDateTime dtIntegracaoFornecedor) {
        this.dtIntegracaoFornecedor = dtIntegracaoFornecedor;
    }

    public String getFgGenerica() {
        return fgGenerica;
    }

    public void setFgGenerica(String fgGenerica) {
        this.fgGenerica = fgGenerica;
    }

    public String getFgEmpresaColigada() {
        return fgEmpresaColigada;
    }

    public void setFgEmpresaColigada(String fgEmpresaColigada) {
        this.fgEmpresaColigada = fgEmpresaColigada;
    }

    public String getDsSenhaB2b() {
        return dsSenhaB2b;
    }

    public void setDsSenhaB2b(String dsSenhaB2b) {
        this.dsSenhaB2b = dsSenhaB2b;
    }

    public String getFgDescontoTractebel() {
        return fgDescontoTractebel;
    }

    public void setFgDescontoTractebel(String fgDescontoTractebel) {
        this.fgDescontoTractebel = fgDescontoTractebel;
    }

    public String getCdSuframa() {
        return cdSuframa;
    }

    public void setCdSuframa(String cdSuframa) {
        this.cdSuframa = cdSuframa;
    }

    public LocalDate getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(LocalDate dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public String getPoLatitude() {
        return poLatitude;
    }

    public void setPoLatitude(String poLatitude) {
        this.poLatitude = poLatitude;
    }

    public String getPoLongitude() {
        return poLongitude;
    }

    public void setPoLongitude(String poLongitude) {
        this.poLongitude = poLongitude;
    }

    public Integer getCdFormaPagamentoTp() {
        return cdFormaPagamentoTp;
    }

    public void setCdFormaPagamentoTp(Integer cdFormaPagamentoTp) {
        this.cdFormaPagamentoTp = cdFormaPagamentoTp;
    }

    public Integer getCdFormaPagamentoTr() {
        return cdFormaPagamentoTr;
    }

    public void setCdFormaPagamentoTr(Integer cdFormaPagamentoTr) {
        this.cdFormaPagamentoTr = cdFormaPagamentoTr;
    }

    public Integer getCdCondicaoPagamentoCp() {
        return cdCondicaoPagamentoCp;
    }

    public void setCdCondicaoPagamentoCp(Integer cdCondicaoPagamentoCp) {
        this.cdCondicaoPagamentoCp = cdCondicaoPagamentoCp;
    }

    public String getNrCrm() {
        return nrCrm;
    }

    public void setNrCrm(String nrCrm) {
        this.nrCrm = nrCrm;
    }

    public String getFgRevisado() {
        return fgRevisado;
    }

    public void setFgRevisado(String fgRevisado) {
        this.fgRevisado = fgRevisado;
    }

    public LocalDateTime getDtUltimaRevisao() {
        return dtUltimaRevisao;
    }

    public void setDtUltimaRevisao(LocalDateTime dtUltimaRevisao) {
        this.dtUltimaRevisao = dtUltimaRevisao;
    }

    public Integer getCdUsuarioRevisao() {
        return cdUsuarioRevisao;
    }

    public void setCdUsuarioRevisao(Integer cdUsuarioRevisao) {
        this.cdUsuarioRevisao = cdUsuarioRevisao;
    }

    public String getFgProdutorRural() {
        return fgProdutorRural;
    }

    public void setFgProdutorRural(String fgProdutorRural) {
        this.fgProdutorRural = fgProdutorRural;
    }

    public String getFgOptanteMei() {
        return fgOptanteMei;
    }

    public void setFgOptanteMei(String fgOptanteMei) {
        this.fgOptanteMei = fgOptanteMei;
    }

    public String getDsSenhaCliente() {
        return dsSenhaCliente;
    }

    public void setDsSenhaCliente(String dsSenhaCliente) {
        this.dsSenhaCliente = dsSenhaCliente;
    }

    public String getFgSituacao() {
        return fgSituacao;
    }

    public void setFgSituacao(String fgSituacao) {
        this.fgSituacao = fgSituacao;
    }

    public String getFgIssSubstituicao() {
        return fgIssSubstituicao;
    }

    public void setFgIssSubstituicao(String fgIssSubstituicao) {
        this.fgIssSubstituicao = fgIssSubstituicao;
    }
}