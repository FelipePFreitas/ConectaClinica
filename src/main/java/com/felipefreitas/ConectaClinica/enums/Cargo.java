package com.felipefreitas.ConectaClinica.enums;

import lombok.Getter;

@Getter
public enum Cargo {

    MEDICO("Medico"),
    ENFERMEIRO("Enfermeiro"),
    RECEPCIONISTA("Recepcionista"),
    GERENTE("Gerente");

    private final String cargo;

    Cargo(String cargo) {
        this.cargo = cargo;
    }
}
