package com.rioDesertoAcessoDb.model;

import jakarta.persistence.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_foto_inspecao")
@Schema(description = "Entidade que representa uma foto de inspeção de piezômetro")
public class FotoInspecao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_foto")
    @Schema(description = "ID único da foto", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idFoto;

    @Column(name = "cd_piezometro", nullable = false)
    @Schema(description = "Código do piezômetro associado", example = "101")
    private Integer cdPiezometro;

    @Column(name = "nm_arquivo", nullable = false, length = 255)
    @Schema(description = "Nome do arquivo da foto", example = "foto_001.jpg")
    private String nmArquivo;

    @Column(name = "caminho_completo", nullable = false, columnDefinition = "TEXT")
    @Schema(description = "Caminho completo do arquivo no servidor")
    private String caminhoCompleto;

    @Column(name = "data_insercao", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Schema(description = "Data e hora de inserção do registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime dataInsercao;
}
