package com.felipefreitas.ConectaClinica.dto.agendamento;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Paciente retornado pela API.")
public record PacienteResponseDTO(
        UUID id,
        String nome
) {
}
