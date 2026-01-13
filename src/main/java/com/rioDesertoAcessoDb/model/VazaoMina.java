package com.rioDesertoAcessoDb.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_vazao_mina")
@Data
public class VazaoMina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "mes_ano_vazao", nullable = false)
    private LocalDate mesAnoVazao;

    @Column(name = "vazao_bombeamento", nullable = false, precision = 10, scale = 2)
    private BigDecimal vazaoBombeamento;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
