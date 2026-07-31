package com.felipefreitas.ConectaClinica.enums;

import lombok.Getter;

@Getter
public enum RoleFuncionario {

    ROLE_ADMIN("Admin"),
    ROLE_FUNCIONARIO("Funcionario"),
    ROLE_GERENTE("Gerente");

    private final String role;

    RoleFuncionario(String role) {
        this.role = role;
    }
}
