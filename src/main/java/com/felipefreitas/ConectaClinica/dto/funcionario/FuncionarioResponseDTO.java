package com.felipefreitas.ConectaClinica.dto.funcionario;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record FuncionarioResponseDTO(

        UUID id,

        String nome,

        String cpf
) {
}
