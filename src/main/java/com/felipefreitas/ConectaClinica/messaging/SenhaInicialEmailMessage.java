package com.felipefreitas.ConectaClinica.messaging;

public record SenhaInicialEmailMessage(
        String nome,
        String email,
        String login,
        String senha
) {
}
