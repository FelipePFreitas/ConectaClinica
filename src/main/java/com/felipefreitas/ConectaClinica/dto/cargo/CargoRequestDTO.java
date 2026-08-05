package com.felipefreitas.ConectaClinica.dto.cargo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CargoRequestDTO(
        
        @NotBlank(message = "Nome do cargo é obrigatório")
        @Size(max = 50, message = "A observação deve ter no máximo 50 caracteres")
        String nome
) {
}
