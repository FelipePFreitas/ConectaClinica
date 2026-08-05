package com.felipefreitas.ConectaClinica.dto.funcionario;

import com.felipefreitas.ConectaClinica.entity.FuncionarioEntity;
import com.felipefreitas.ConectaClinica.enums.RoleFuncionario;

public record FuncionarioResponseDTO(
        Long id,
        String nome,
        String cpf,
        String email,
        String cargo, // 👈 Em vez de cargoId e nomeCargo, deixamos apenas o nome do cargo
        RoleFuncionario role,
        boolean ativo,
        String registroProfissional,
        String especialidade // 👈 Aplicado o mesmo padrão para especialidade
) {
    public FuncionarioResponseDTO(FuncionarioEntity funcionarioEntity) {
        this(
                funcionarioEntity.getId(),
                funcionarioEntity.getNome(),
                funcionarioEntity.getCpf(),
                funcionarioEntity.getEmail(),
                funcionarioEntity.getCargo() != null ? funcionarioEntity.getCargo().getNome() : null,
                funcionarioEntity.getRole(),
                funcionarioEntity.isAtivo(),
                funcionarioEntity.getRegistroProfissional(),
                funcionarioEntity.getEspecialidade() != null ? funcionarioEntity.getEspecialidade().getNome() : null
        );
    }
}