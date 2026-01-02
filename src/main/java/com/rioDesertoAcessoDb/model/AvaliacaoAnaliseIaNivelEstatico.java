package com.rioDesertoAcessoDb.model;

import jakarta.persistence.*;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Entity
@Table(name = "avaliacoes_analise_ia_nivel_estatico")
@Schema(description = "Entidade que representa uma avaliação de análise de IA para nível estático")
public class AvaliacaoAnaliseIaNivelEstatico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avaliacao")
    @Schema(description = "ID único da avaliação", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idAvaliacao;

    @Column(name = "id_piezometro", nullable = false)
    @Schema(description = "Código do piezômetro associado", example = "206")
    private Integer cdPiezometro;

    @Column(name = "editou_analise", nullable = false)
    @Schema(description = "Indica se a análise foi editada pelo usuário")
    private Boolean editouAnalise;

    @Column(name = "analise_original", nullable = false, columnDefinition = "TEXT")
    @Schema(description = "Conteúdo original da análise gerada pela IA")
    private String analiseOriginal;

    @Column(name = "analise_editada", columnDefinition = "TEXT")
    @Schema(description = "Conteúdo da análise após edição do usuário")
    private String analiseEditada;

    @Column(name = "nota")
    @Schema(description = "Nota atribuída à análise (ex: 1 a 5)", example = "5")
    private Integer nota;

    @Column(name = "comentario", length = 1000)
    @Schema(description = "Comentário adicional sobre a avaliação")
    private String comentario;

    @Column(name = "ia_analisou", nullable = false)
    @Schema(description = "Indica se a IA já analisou este registro", example = "false")
    private Boolean iaAnalisou = false;
}
