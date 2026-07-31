package com.felipefreitas.ConectaClinica.dto.tipoexame;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TipoExameRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 50, message = "Nome não pode passar de 50 caracteres")
        String exame,

        @NotBlank(message = "A descrição é obrigatória")
        @Size(max = 100, message = "A descrição deve ter no máximo 100 caracteres")
        String descricao,

        @NotNull(message = "A especialidade é obrigatória")
        Long IdEspecialidades,

        @NotNull(message = "A duração é obrigatória")
        @Min(value = 1, message = "A duração deve ser de no mínimo 1 minuto")
        Integer duracao

) {
}
