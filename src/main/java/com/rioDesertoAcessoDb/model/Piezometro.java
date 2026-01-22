package com.rioDesertoAcessoDb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TB_PIEZOMETRO")
@Schema(description = "Modelo que representa um Piezômetro")
public class Piezometro {

    @Id
    @Column(name = "CD_PIEZOMETRO")
    @Schema(description = "Código interno do piezômetro", example = "1")
    @JsonProperty("cdPiezometro")
    private Integer cdPiezometro;

    @Column(name = "CD_EMPRESA", nullable = false)
    @Schema(description = "Código da empresa vinculada", example = "18")
    @JsonIgnore
    private Integer cdEmpresa;

    @Column(name = "CD_BACIA")
    @Schema(description = "Código da bacia hidrográfica", example = "10")
    @JsonIgnore
    private Integer cdBacia;

    @Column(name = "ID_PIEZOMETRO", nullable = false, length = 100)
    @Schema(description = "Identificador único (sigla/número) do piezômetro", example = "PZ-01")
    @JsonProperty("idPiezometro")
    private String idPiezometro;

    @Column(name = "NM_PIEZOMETRO", nullable = false, length = 150)
    @Schema(description = "Nome descritivo do piezômetro", example = "Piezômetro de Mina 01")
    @JsonProperty("nomePiezometro")
    private String nmPiezometro;

    @Column(name = "TP_PIEZOMETRO", nullable = false, length = 2)
    @Schema(description = "Tipo do piezômetro (PP, PR, PC, PV, PB, etc.)", example = "PP")
    @JsonProperty("tipoPiezometro")
    private String tpPiezometro;

    // @Column(name = "DS_OBSERVACAO") //NÃO APAGUE ESSE COMENTÁRIO
    // @Lob
    // private String dsObservacao;
    // tem que converter esse cara, deixar comentado por enquanto, se precisarmos
    // vou atrás, vou deixar no código para a gente não esquecer que existe

    @Column(name = "FG_SITUACAO", nullable = false, length = 1)
    @Schema(description = "Situação do piezômetro (A=Ativo, I=Inativo)", example = "A")
    @JsonProperty("situacaoPiezometro")
    private String fgSituacao;

    @Column(name = "CD_USUARIO_INCLUSAO", nullable = false)
    @Schema(description = "ID do usuário que realizou a inclusão")
    @JsonIgnore
    private Integer cdUsuarioInclusao;

    @Column(name = "DT_INCLUSAO", nullable = false)
    @Schema(description = "Data e hora da inclusão no sistema")
    @JsonIgnore
    private LocalDateTime dtInclusao;

    @Column(name = "CD_USUARIO_ALTERACAO", nullable = false)
    @Schema(description = "ID do usuário que realizou a última alteração")
    @JsonIgnore
    private Integer cdUsuarioAlteracao;

    @Column(name = "DT_ALTERACAO", nullable = false)
    @Schema(description = "Data e hora da última alteração no sistema")
    @JsonIgnore
    private LocalDateTime dtAlteracao;
}