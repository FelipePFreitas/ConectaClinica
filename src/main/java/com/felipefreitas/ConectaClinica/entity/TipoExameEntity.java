package com.felipefreitas.ConectaClinica.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "exame", nullable = false, unique = true,length = 50)
    private String exame;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @JoinColumn(name = "especialidade_id",referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private EspecialidadeEntity especialidade;

    @Column(name = "duracao", nullable = false)
    private Integer duracao;

    @Column(name = "ativo", nullable = false)
    @Builder.Default
    private boolean ativo = true;

}
