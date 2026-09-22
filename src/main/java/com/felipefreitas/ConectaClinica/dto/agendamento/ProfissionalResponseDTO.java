package com.felipefreitas.ConectaClinica.dto.agendamento;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Profissional retornado pela API.")
public record ProfissionalResponseDTO(
        UUID id,
        String nome,
        boolean ativo
) {
}
