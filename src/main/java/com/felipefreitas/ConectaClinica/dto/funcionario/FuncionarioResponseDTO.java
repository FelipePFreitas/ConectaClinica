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
    public FuncionarioResponseDTO(FuncionarioEntity f) {
        this(
                f.getId(),
                f.getNome(),
                f.getCpf(),
                f.getEmail(),
                f.getCargo() != null ? f.getCargo().getNome() : null,
                f.getRole(),
                f.isAtivo(),
                f.getRegistroProfissional(),
                f.getEspecialidade() != null ? f.getEspecialidade().getNome() : null
        );
    }
}