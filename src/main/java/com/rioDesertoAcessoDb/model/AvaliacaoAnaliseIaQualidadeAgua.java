package com.rioDesertoAcessoDb.model;

import jakarta.persistence.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "avaliacoes_analise_ia_qualidade_agua")
@Schema(description = "Entidade que representa uma avaliação de análise de IA para qualidade da água")
public class AvaliacaoAnaliseIaQualidadeAgua {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avaliacao")
    @Schema(description = "ID único da avaliação", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idAvaliacao;

    @Column(name = "id_zeus", nullable = false)
    @Schema(description = "ID da análise no sistema Zeus associado", example = "12345")
    private Integer idZeus;

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
    @Schema(description = "Nota atribuída à análise (ex: 1 a 10)", example = "10")
    private Integer nota;

    @Column(name = "comentario", length = 1000)
    @Schema(description = "Comentário adicional sobre a avaliação")
    private String comentario;

    @Column(name = "criado_em", insertable = false, updatable = false)
    @Schema(description = "Data e hora de criação da avaliação", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime criadoEm;
}
