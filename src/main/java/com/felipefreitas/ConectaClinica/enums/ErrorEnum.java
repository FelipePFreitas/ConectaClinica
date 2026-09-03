package com.felipefreitas.ConectaClinica.enums;

import lombok.Getter;

@Getter
public enum ErrorEnum {
    // Regras simples e validações gerais (faixa 1-99)
    CAMPO_OBRIGATORIO(400, 1, "Campo obrigatório não pode ser nulo ou em branco"),
    VALOR_INVALIDO(400, 2, "Valor informado é inválido"),
    FORMATO_INVALIDO(400, 3, "Formato informado é inválido"),
    LIMITE_CARACTERES_EXCEDIDO(400, 4, "Limite de caracteres excedido"),
    RECURSO_NAO_ENCONTRADO(404, 5, "Recurso não encontrado"),

    // Autenticação e usuários (faixa 100-199)
    CREDENCIAIS_INVALIDAS(401, 100, "Credenciais inválidas"),
    TOKEN_INVALIDO(401, 101, "Token inválido ou expirado"),
    USUARIO_NAO_ENCONTRADO(404, 102, "Usuário não encontrado"),
    LOGIN_JA_CADASTRADO(409, 103, "Login já cadastrado"),
    CPF_JA_CADASTRADO(409, 104, "CPF já cadastrado");

    private final int httpStatus;
    private final int errorCode;
    private final String errorMessage;

    ErrorEnum(int httpStatus, int errorCode, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}