package com.rioDesertoAcessoDb.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TB_PIEZOMETRO")
public class Piezometro {

    @Id
    @Column(name = "CD_PIEZOMETRO")
    private Integer cdPiezometro;

    @Column(name = "CD_EMPRESA", nullable = false)
    private Integer cdEmpresa;

    @Column(name = "CD_BACIA")
    private Integer cdBacia;

    @Column(name = "ID_PIEZOMETRO", nullable = false, length = 100)
    private String idPiezometro;

    @Column(name = "NM_PIEZOMETRO", nullable = false, length = 150)
    private String nmPiezometro;

    @Column(name = "TP_PIEZOMETRO", nullable = false, length = 2)
    private String tpPiezometro;

//    @Column(name = "DS_OBSERVACAO") //NÃO APAGUE ESSE COMENTÁRIO
//    @Lob
//    private String dsObservacao;
// tem que converter esse cara, deixar comentado por enquanto, se precisarmos vou atrás, vou deixar no código para a gente não esquecer que existe

    @Column(name = "FG_SITUACAO", nullable = false, length = 1)
    private String fgSituacao;

    @Column(name = "CD_USUARIO_INCLUSAO", nullable = false)
    private Integer cdUsuarioInclusao;

    @Column(name = "DT_INCLUSAO", nullable = false)
    private LocalDateTime dtInclusao;

    @Column(name = "CD_USUARIO_ALTERACAO", nullable = false)
    private Integer cdUsuarioAlteracao;

    @Column(name = "DT_ALTERACAO", nullable = false)
    private LocalDateTime dtAlteracao;
}