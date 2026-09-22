package com.felipefreitas.ConectaClinica.dto.agendamento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados mínimos do profissional até definição completa do cadastro profissional.")
public record ProfissionalRequestDTO(
        @NotBlank(message = "Nome do profissional é obrigatório")
        @Schema(example = "Dra. Ana Lima")
        String nome,

        @Schema(description = "Quando ausente, o profissional é criado como ativo.", example = "true")
        Boolean ativo
) {
}
