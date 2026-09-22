package com.felipefreitas.ConectaClinica.dto.agendamento;

import java.util.UUID;

public record PacienteResumoDTO(
        UUID id,
        String nome
) {
}
