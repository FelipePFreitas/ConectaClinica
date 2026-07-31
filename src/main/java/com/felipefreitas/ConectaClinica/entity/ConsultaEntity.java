package com.felipefreitas.ConectaClinica.entity;

import com.felipefreitas.ConectaClinica.enums.StatusAgendamento;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "consultas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultaEntity {

    // 1. CHAVE PRIMÁRIA
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 2. CHAVES ESTRANGEIRAS (Relacionamentos agrupados)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private PacienteEntity paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", nullable = false)
    private MedicoEntity medico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "especialidade_id", nullable = false)
    private EspecialidadeEntity especialidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_exame_id", nullable = false)
    private TipoExameEntity tipoExame;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id", nullable = false)
    private FuncionarioEntity criadoPor;

    // 3. CAMPOS DE DADOS DA CONSULTA
    @Column(name = "observacao", nullable = false, length = 100)
    private String observacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_agendamento", nullable = false)
    @Builder.Default
    private StatusAgendamento statusAgendamento = StatusAgendamento.AGENDADO;

    // 4. DATAS DO SISTEMA
    @Column(name = "data_consulta", nullable = false)
    private LocalDateTime dataConsulta;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime dataCadastro = LocalDateTime.now();
}