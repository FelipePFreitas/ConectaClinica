package com.felipefreitas.ConectaClinica.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tipos_exames")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TipoExameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exame", nullable = false, unique = true, length = 50)
    private String exame;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "duracao", nullable = false)
    private Integer duracao;

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "ativo", nullable = false)
    @Builder.Default
    private boolean ativo = true;

}
