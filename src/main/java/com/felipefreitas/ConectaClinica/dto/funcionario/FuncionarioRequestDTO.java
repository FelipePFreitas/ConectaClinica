package com.felipefreitas.ConectaClinica.dto.funcionario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record FuncionarioRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        String nome,


        @NotBlank(message = "CPF é obrigatório")
        String cpf
) {
}
