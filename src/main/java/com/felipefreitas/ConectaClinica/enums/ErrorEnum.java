package com.felipefreitas.ConectaClinica.enums;

import lombok.Getter;

@Getter
public enum ErrorEnum {

    // 0-99: Erros Gerais, Entrada e Autenticação/Autorização (400, 401, 403, 404, 500)
    DADOS_INVALIDOS(400, 1, "Dados da requisição inválidos ou mal formatados"),
    NAO_AUTORIZADO(401, 2, "Token de acesso ausente, inválido ou expirado"),
    ACESSO_NEGADO(403, 3, "Você não tem permissão para acessar este recurso"),
    CREDENCIAIS_INVALIDAS(401, 4, "E-mail ou senha incorretos"),
    RECURSO_NAO_ENCONTRADO(404, 5, "Recurso solicitado não foi encontrado"),
    ERRO_INTERNO_SERVIDOR(500, 6, "Ocorreu um erro interno inesperado no servidor"),
    CPF_INVALIDO(400, 7, "O CPF informado é inválido"),
    ACAO_NAO_PERMITIDA(400, 8, "Ação não permitida"),

    // 100-199: Erros de Funcionário / Cargo / Autenticação de Usuário
    FUNCIONARIO_NAO_ENCONTRADO(404, 100, "Funcionário não encontrado"),
    EMAIL_FUNCIONARIO_JA_CADASTRADO(409, 101, "O e-mail informado já está cadastrado para outro funcionário"),
    CPF_FUNCIONARIO_JA_CADASTRADO(409, 102, "O CPF informado já está cadastrado para outro funcionário"),
    FUNCIONARIO_INATIVO(400, 103, "O funcionário informado está inativo no sistema"),
    CARGO_NAO_CADASTRADO(404, 104, "O cargo informado não está cadastrado no sistema"),
    CARGO_NAO_ENCONTRADO(404, 104, "Cargo não encontrado"),
    CARGO_JA_CADASTRADO(409, 105, "Já existe um cargo cadastrado com este nome"),
    ROLE_NAO_CADASTRADO(404, 106, "O role informado não está cadastrado no sistema"),
    CARGO_POSSUI_FUNCIONARIOS_VINCULADOS(409, 107, "O cargo não pode ser excluído pois possui funcionários vinculados"),

    // 200-299: Erros de Médico e Registro Profissional
    MEDICO_NAO_ENCONTRADO(404, 200, "Médico não encontrado"),
    REGISTRO_PROFISSIONAL_OBRIGATORIO(400, 201, "O Registro Profissional é obrigatório para esta função"),
    REGISTRO_PROFISSIONAL_JA_CADASTRADO(409, 202, "O Registro Profissional já está cadastrado para outro médico/profissional"),
    EMAIL_MEDICO_JA_CADASTRADO(409, 203, "O e-mail informado já está cadastrado para outro médico"),
    MEDICO_INATIVO(400, 204, "O médico informado está inativo e não pode receber novos agendamentos"),

    // 300-399: Erros de Paciente
    PACIENTE_NAO_ENCONTRADO(404, 300, "Paciente não encontrado"),
    CPF_PACIENTE_JA_CADASTRADO(409, 301, "O CPF informado já está cadastrado para outro paciente"),
    EMAIL_PACIENTE_JA_CADASTRADO(409, 302, "O e-mail informado já está cadastrado para outro paciente"),
    PACIENTE_INATIVO(400, 303, "O paciente informado está inativo no sistema"),

    // 400-499: Erros de Especialidade e TipoExame
    ESPECIALIDADE_NAO_ENCONTRADA(404, 400, "Especialidade não encontrada"),
    ESPECIALIDADE_JA_CADASTRADA(409, 401, "Já existe uma especialidade cadastrada com este nome"),
    ESPECIALIDADE_POSSUI_MEDICOS_VINCULADOS(409, 402, "A especialidade não pode ser excluída pois possui médicos vinculados"),
    TIPO_EXAME_NAO_ENCONTRADO(404, 403, "Tipo de exame não encontrado"),
    TIPO_EXAME_JA_CADASTRADO(409, 404, "Já existe um tipo de exame cadastrado com este nome"),

    // 500-599: Erros de Regra de Negócio de Consultas e Agendamentos
    CONSULTA_NAO_ENCONTRADA(404, 500, "Consulta/Agendamento não encontrado"),
    DATA_CONSULTA_INVALIDA(400, 501, "A data e hora da consulta devem ser no futuro"),
    MEDICO_OCUPADO_HORARIO(422, 502, "O médico selecionado já possui um agendamento neste mesmo horário"),
    PACIENTE_OCUPADO_HORARIO(422, 503, "O paciente já possui uma consulta ou exame agendado neste mesmo horário"),
    CONSULTA_JA_CANCELADA(422, 504, "A consulta selecionada já se encontra cancelada"),
    CONSULTA_JA_REALIZADA(422, 505, "A consulta não pode ser alterada ou cancelada pois já foi realizada");

    private final int httpStatus;
    private final int errorCode;
    private final String errorMessage;

    ErrorEnum(int httpStatus, int errorCode, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}