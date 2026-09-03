package com.felipefreitas.ConectaClinica.dto.funcionario;

import java.util.UUID;

public record FuncionarioResponseDTO(

        UUID id,

        String nome,

        String cpf,

        String email
) {
}
