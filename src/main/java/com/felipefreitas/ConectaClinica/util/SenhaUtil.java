package com.felipefreitas.ConectaClinica.util;

public class SenhaUtil {

    public static String geradorSenhaAleatoria(int tamanho) {

        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";

        if (tamanho < 6) {
            throw new IllegalArgumentException("O tamanho da senha deve ser no mínimo 6 caracteres.");
        }
        StringBuilder senha = new StringBuilder();
        for (int i = 0; i < tamanho; i++) {
            int indice = (int) (Math.random() * caracteres.length());
            senha.append(caracteres.charAt(indice));
        }
        return senha.toString();
    }
}
