package com.felipefreitas.ConectaClinica.dto.agendamento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Dados para criar bloqueio de agenda do profissional.")
public record BloqueioAgendaCriacaoRequestDTO(
        @NotNull(message = "Profissional é obrigatório")
        UUID profissionalId,

        @NotNull(message = "Início do bloqueio é obrigatório")
        @Future(message = "Início do bloqueio deve ser futuro")
        OffsetDateTime inicio,

        @NotNull(message = "Fim do bloqueio é obrigatório")
        @Future(message = "Fim do bloqueio deve ser futuro")
        OffsetDateTime fim,

        @NotBlank(message = "Motivo do bloqueio é obrigatório")
        String motivo
) {
}
