package com.rioDesertoAcessoDb.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "analise")
public class Analise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_analise")
    private Integer idAnalise;

    @Column(name = "simbolo", nullable = false, length = 8)
    private String simbolo;

    @Column(name = "laboratorio", nullable = false)
    private Short laboratorio;

    @Column(name = "indice", nullable = false)
    private Integer indice;

    @Column(name = "nome", nullable = false, length = 80)
    private String nome;

    @Column(name = "unidade", length = 50)
    private String unidade;

    @Column(name = "custo", precision = 15, scale = 2)
    private BigDecimal custo;

    @Column(name = "ind_cad")
    private Integer indCad;

    @Column(name = "procedimento", length = 50)
    private String procedimento;

    @Column(name = "rca", length = 20)
    private String rca;

    @Column(name = "dea", length = 20)
    private String dea;

    @Column(name = "rce", length = 20)
    private String rce;

    @Column(name = "dee", length = 20)
    private String dee;

    @Column(name = "divisao", length = 20)
    private String divisao;

    @Column(name = "const_a", precision = 15, scale = 4)
    private BigDecimal constA;

    @Column(name = "const_b", precision = 15, scale = 4)
    private BigDecimal constB;

    @Column(name = "min_detectavel", length = 17)
    private String minDetectavel;

    @Column(name = "metodologia", length = 75)
    private String metodologia;

    @Column(name = "portaria518", length = 25)
    private String portaria518;

    @Column(name = "granulometrica")
    private Short granulometrica;

    @Column(name = "unidade_conv", length = 50)
    private String unidadeConv;

    @Column(name = "nome_i", length = 80)
    private String nomeI;

    @Column(name = "nome_e", length = 80)
    private String nomeE;

    @Column(name = "lquantifica", precision = 15, scale = 4)
    private BigDecimal lquantifica;

    @Column(name = "ldetecta", precision = 15, scale = 4)
    private BigDecimal ldetecta;

    @Column(name = "incerteza", precision = 15, scale = 3)
    private BigDecimal incerteza;

    @Column(name = "metodo", length = 200)
    private String metodo;

    @Column(name = "casasdecimais")
    private Short casasDecimais;
}
