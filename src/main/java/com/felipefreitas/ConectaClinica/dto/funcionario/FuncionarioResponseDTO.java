package com.felipefreitas.ConectaClinica.dto.funcionario;

import com.felipefreitas.ConectaClinica.enums.RoleFuncionario;

public record FuncionarioResponseDTO(
        Long id,
        String nome,
        String cpf,
        String email,
        Cargo cargo,
        RoleFuncionario role,
        boolean ativo) {
}
