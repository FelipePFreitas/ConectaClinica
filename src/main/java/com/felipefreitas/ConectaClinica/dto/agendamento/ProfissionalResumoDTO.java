package com.felipefreitas.ConectaClinica.dto.agendamento;

import java.util.UUID;

public record ProfissionalResumoDTO(
        UUID id,
        String nome,
        boolean ativo
) {
}
