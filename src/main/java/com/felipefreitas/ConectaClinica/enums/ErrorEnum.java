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
    CPF_JA_CADASTRADO(409, 104, "CPF já cadastrado"),
    EMAIL_JA_CADASTRADO(409, 105, "Email já cadastrado para outro funcionário"),

    // Agendamento de consultas (faixa 200-299)
    PACIENTE_NAO_ENCONTRADO(404, 200, "Paciente não encontrado"),
    PROFISSIONAL_NAO_ENCONTRADO(404, 201, "Profissional não encontrado"),
    PROFISSIONAL_INATIVO(422, 202, "Profissional inativo não pode receber agendamentos"),
    CONSULTA_NAO_ENCONTRADA(404, 203, "Consulta não encontrada"),
    CONFLITO_AGENDA_PACIENTE(409, 204, "Paciente já possui consulta ativa sobreposta neste horário"),
    CONFLITO_AGENDA_PROFISSIONAL(409, 205, "Profissional já possui consulta ativa ou bloqueio sobreposto neste horário"),
    TRANSICAO_STATUS_INVALIDA(422, 206, "Transição de status da consulta não permitida"),
    CONSULTA_ENCERRADA(422, 207, "Consulta encerrada não pode ser alterada"),
    BLOQUEIO_AGENDA_NAO_ENCONTRADO(404, 208, "Bloqueio de agenda não encontrado"),
    PERMISSAO_AGENDAMENTO_NEGADA(403, 209, "Usuário não possui permissão para administrar agendamentos"),
    PERIODO_AGENDA_INVALIDO(400, 210, "Período da agenda é inválido");

    private final int httpStatus;
    private final int errorCode;
    private final String errorMessage;

    ErrorEnum(int httpStatus, int errorCode, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}