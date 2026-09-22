package com.felipefreitas.ConectaClinica.entity;

import com.felipefreitas.ConectaClinica.enums.AcaoHistoricoConsulta;
import com.felipefreitas.ConectaClinica.enums.StatusConsulta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "historicos_consulta")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HistoricoConsultaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "consulta_id", nullable = false)
    private ConsultaEntity consulta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private AcaoHistoricoConsulta acao;

    @Enumerated(EnumType.STRING)
    @Column(length = 40)
    private StatusConsulta statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private StatusConsulta statusPosterior;

    @Column(nullable = false)
    private String autor;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime ocorridoEm;

    private String detalhe;
}
