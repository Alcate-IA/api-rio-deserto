package com.rioDesertoAcessoDb.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "parametros_legislacao")
public class ParametrosLegislacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parametro_legislacao")
    private Integer idParametroLegislacao;

    @Column(name = "id_analise", nullable = false)
    private Integer idAnalise;

    @Column(name = "id_legislacao", nullable = false)
    private Integer idLegislacao;

    @Column(name = "parametro", nullable = false, length = 255)
    private String parametro;
}
