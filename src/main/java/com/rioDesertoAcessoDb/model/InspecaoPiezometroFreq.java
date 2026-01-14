package com.rioDesertoAcessoDb.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TB_INSPECAO_PIEZOMETRO_FREQ")
public class InspecaoPiezometroFreq {

    @Id
    @Column(name = "CD_INSPECAO_PIEZOMETRO_FREQ")
    private Integer cdInspecaoPiezometroFreq;

    @Column(name = "CD_PIEZOMETRO", nullable = false)
    private Integer cdPiezometro;

    @Column(name = "TP_FREQUENCIA_OLD", length = 1)
    private String tpFrequenciaOld;

    @Column(name = "TP_FREQUENCIA_NEW", length = 1)
    private String tpFrequenciaNew;

    @Column(name = "CD_USUARIO_INCLUSAO", nullable = false)
    private Integer cdUsuarioInclusao;

    @Column(name = "DT_INCLUSAO", nullable = false)
    private LocalDateTime dtInclusao;
}
