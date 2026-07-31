package com.felipefreitas.ConectaClinica.dto.medico;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MedicoRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "Nome não pode passar de 100 caracteres")
        String nome,

        @NotBlank(message = "O crm é obrigatório")
        @Size(max = 13, message = "O crm não pode passar de 13 caracteres")
        String crm,

        @NotNull(message = "A especialidade é obrigatória")
        Long idEspecialidade,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O formato de email é inválido")
        String email,

        @NotBlank(message = "O telefone é obrigatório")
        @Size(max = 20, message = "O telefone não pode passar de 20 caracteres")
        String telefone
) {
}
