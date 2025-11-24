package com.rioDesertoAcessoDb.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_calibracao_equipamento")
public class CalibracaoEquipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_calibracao_equipamento")
    private Integer cdCalibracaoEquipamento;

    @Column(name = "cd_empresa", nullable = false)
    private Integer cdEmpresa;

    @Column(name = "cd_empresa_setor")
    private Integer cdEmpresaSetor;

    @Column(name = "cd_orgao")
    private Integer cdOrgao;

    @Column(name = "cd_unidade_medida_equipamento")
    private Integer cdUnidadeMedidaEquipamento;

    @Column(name = "nr_equipamento", nullable = false, length = 20)
    private String nrEquipamento;

    @Column(name = "ds_equipamento", nullable = false, length = 80)
    private String dsEquipamento;

    @Column(name = "ds_marca", length = 80)
    private String dsMarca;

    @Column(name = "vl_menor_divisao", precision = 16, scale = 6)
    private BigDecimal vlMenorDivisao;

    @Column(name = "vl_faixa_indicacao_inicial", precision = 16, scale = 6)
    private BigDecimal vlFaixaIndicacaoInicial;

    @Column(name = "vl_faixa_indicacao_final", precision = 16, scale = 6)
    private BigDecimal vlFaixaIndicacaoFinal;

    @Column(name = "vl_exatidao_requerida", precision = 16, scale = 6)
    private BigDecimal vlExatidaoRequerida;

    @Column(name = "qt_frequencia_calibracao_meses")
    private Integer qtFrequenciaCalibracaoMeses;

    @Column(name = "cd_usuario_inclusao", nullable = false)
    private Integer cdUsuarioInclusao;

    @Column(name = "dt_inclusao", nullable = false)
    private LocalDateTime dtInclusao;

    @Column(name = "cd_usuario_alteracao", nullable = false)
    private Integer cdUsuarioAlteracao;

    @Column(name = "dt_alteracao", nullable = false)
    private LocalDateTime dtAlteracao;

    @Column(name = "ds_observacao", length = 255)
    private String dsObservacao;

    @Column(name = "cd_equipamento")
    private Integer cdEquipamento;

    @Column(name = "fg_situacao", length = 1)
    private String fgSituacao;

    @Column(name = "vl_faixa_indicacao", length = 100)
    private String vlFaixaIndicacao;

    @Column(name = "vl_faixa_uso_equip", length = 100)
    private String vlFaixaUsoEquip;
}