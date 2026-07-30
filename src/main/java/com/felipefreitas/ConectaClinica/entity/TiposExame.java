package com.felipefreitas.ConectaClinica.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipos_exame")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TiposExame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exame", nullable = false, unique = true)
    private String exame;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @JoinColumn(name = "especialidade_id",   referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private EspecialidadeEntity especialidades;

    @Column(name = "duracao", nullable = false)
    private int duracao;

    @Column(name = "preco", nullable = false)
    private Double preco;

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

}
