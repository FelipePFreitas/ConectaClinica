package com.felipefreitas.ConectaClinica.enums;

import lombok.Getter;

@Getter
public enum RoleFuncionario {

    ROLE_ADMIN("Admin"),
    ROLE_ADMINISTRATIVO("Administrativo"),
    ROLE_MEDICO("Médico"),
    ROLE_ENFERMAGEM("Enfermagem"),
    ROLE_GERENTE("Gerente");

    private final String role;

    RoleFuncionario(String role) {
        this.role = role;
    }
}
