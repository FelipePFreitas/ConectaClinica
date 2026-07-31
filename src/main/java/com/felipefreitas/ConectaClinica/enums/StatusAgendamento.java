package com.felipefreitas.ConectaClinica.enums;

import lombok.Getter;

@Getter
public enum StatusAgendamento {

    AGENDADO("Agendado"),
    CONFIRMADO("Confirmado"),
    CANCELADO("Cancelado"),
    REALIZADO("Realizado");

    private final String status;

    StatusAgendamento(String status) {
        this.status = status;
    }
}
