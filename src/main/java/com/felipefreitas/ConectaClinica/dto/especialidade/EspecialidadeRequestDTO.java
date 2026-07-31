package com.felipefreitas.ConectaClinica.dto.especialidade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EspecialidadeRequestDTO(

        @NotBlank(message = "A especialidade não pode estar em branco")
        @Size(max = 50, message = "Especialidade não pode passar de 50 caracteres")
        String especialidade) {
}
