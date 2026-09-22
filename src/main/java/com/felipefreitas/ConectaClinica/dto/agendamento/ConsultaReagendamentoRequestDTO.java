package com.felipefreitas.ConectaClinica.dto.agendamento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.OffsetDateTime;

@Schema(description = "Solicitação de reagendamento. Duração ausente mantém a duração atual.")
public record ConsultaReagendamentoRequestDTO(
        @NotNull(message = "Novo início é obrigatório")
        @Future(message = "Novo início deve ser futuro")
        @Schema(example = "2026-09-24T11:00:00-03:00")
        OffsetDateTime inicio,

        @Positive(message = "Duração da consulta deve ser positiva")
        Integer duracaoMinutos,

        String motivo
) {
}
