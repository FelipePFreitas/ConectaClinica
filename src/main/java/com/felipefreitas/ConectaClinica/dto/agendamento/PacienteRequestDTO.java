package com.felipefreitas.ConectaClinica.dto.agendamento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados mínimos do paciente até definição completa do cadastro.")
public record PacienteRequestDTO(
        @NotBlank(message = "Nome do paciente é obrigatório")
        @Schema(example = "Maria Souza")
        String nome
) {
}
