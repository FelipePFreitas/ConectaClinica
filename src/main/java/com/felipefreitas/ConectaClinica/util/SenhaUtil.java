package com.felipefreitas.ConectaClinica.util;

import java.security.SecureRandom;

public class SenhaUtil {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String CARACTERES =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";

    public static String geradorSenhaAleatoria(int tamanho) {

        if (tamanho < 6) {
            throw new IllegalArgumentException("O tamanho da senha deve ser no mínimo 6 caracteres.");
        }

        StringBuilder senha = new StringBuilder(tamanho);
        for (int i = 0; i < tamanho; i++) {
            senha.append(CARACTERES.charAt(RANDOM.nextInt(CARACTERES.length())));
        }
        return senha.toString();
    }
}
