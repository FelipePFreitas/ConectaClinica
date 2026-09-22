package com.felipefreitas.ConectaClinica.dto.agendamento;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Bloqueio de agenda retornado pela API.")
public record BloqueioAgendaResponseDTO(
        UUID id,
        ProfissionalResumoDTO profissional,
        OffsetDateTime inicio,
        OffsetDateTime fim,
        String motivo,
        String criadoPor,
        OffsetDateTime criadoEm
) {
}
