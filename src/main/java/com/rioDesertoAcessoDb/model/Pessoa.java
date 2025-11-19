package com.rioDesertoAcessoDb.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pessoa {

    // Coordenadas
    private Double longitude;
    private Double latitude;

    // Documentos
    private String rg;
    private String inscricaoInss;
    private String inscricaoMunicipal;
    private String inscricaoEstadual;
    private String crm;
    private String cpf;
    private String cnpj;

    // Endereço / CEP
    private String cepFaturamento;
    private String cep;

    // Nome / Fantasia
    private String nome;
    private String nomeFantasia;

    // Bairro
    private String bairroFaturamento;
    private String bairro;

    // Flags
    private String situacao;
    private String revisado;
    private String retemISS;
    private String produtorRural;
    private String optanteSimples;
    private String optanteMEI;
    private String issSubstituicao;
    private String isentoICMS;
    private String incideICMSST;
    private String incideFunrural;
    private String generica;
    private String pessoaTipo;
    private String empresaColigada;
    private String descontoTractebel;
    private String cooperativa;

    // Datas
    private LocalDate dataUltimaRevisao;
    private LocalDate dataNascimento;
    private LocalDate dataIntegracaoFornecedor;
    private LocalDate dataIntegracaoCliente;
    private LocalDate dataInclusao;
    private LocalDate dataAlteracao;

    // Senhas
    private String senhaCliente;
    private String senhaB2B;

    // Endereço
    private String enderecoFaturamento;
    private String endereco;
    private String complementoFaturamento;
    private String complemento;

    // Usuários
    private String usuarioRevisao;
    private String usuarioInclusao;
    private String usuarioAlteracao;

    private String suframa;

    // Chaves
    private Long codigoPessoaOld;
    private Long codigoPessoa;

    // Formas de pagamento / Empresa
    private Long formaPagamentoTr;
    private Long formaPagamentoTp;
    private Long empresaFilialOld;
    private Long condicaoPagamentoCp;

    private Long cidadeFaturamento;
    private Long cidade;
}
