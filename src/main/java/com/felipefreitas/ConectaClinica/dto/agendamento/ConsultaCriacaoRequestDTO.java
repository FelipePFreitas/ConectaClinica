package com.felipefreitas.ConectaClinica.dto.agendamento;

import com.felipefreitas.ConectaClinica.enums.ModalidadeConsulta;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Dados para criar uma consulta já agendada.")
public record ConsultaCriacaoRequestDTO(
        @NotNull(message = "Paciente é obrigatório")
        UUID pacienteId,

        @NotNull(message = "Profissional é obrigatório")
        UUID profissionalId,

        @NotNull(message = "Início da consulta é obrigatório")
        @Future(message = "Início da consulta deve ser futuro")
        @Schema(example = "2026-09-23T10:00:00-03:00")
        OffsetDateTime inicio,

        @NotNull(message = "Duração da consulta é obrigatória")
        @Positive(message = "Duração da consulta deve ser positiva")
        @Schema(example = "30")
        Integer duracaoMinutos,

        @NotNull(message = "Modalidade da consulta é obrigatória")
        ModalidadeConsulta modalidade,

        @NotBlank(message = "Local ou canal do atendimento é obrigatório")
        @Schema(example = "Sala 2")
        String localOuCanal
) {
}
