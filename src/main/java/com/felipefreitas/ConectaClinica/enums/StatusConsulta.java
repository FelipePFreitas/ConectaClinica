package com.felipefreitas.ConectaClinica.enums;

import java.util.EnumSet;
import java.util.Set;

public enum StatusConsulta {
    SOLICITADA,
    AGENDADA,
    CONFIRMADA,
    EM_ATENDIMENTO,
    CONCLUIDA,
    CANCELADA_PELO_PACIENTE,
    CANCELADA_PELA_CLINICA,
    REAGENDAMENTO_SOLICITADO,
    NAO_COMPARECEU;

    private static final Set<StatusConsulta> STATUS_ATIVOS = EnumSet.of(
            SOLICITADA,
            AGENDADA,
            CONFIRMADA,
            EM_ATENDIMENTO,
            REAGENDAMENTO_SOLICITADO
    );

    private static final Set<StatusConsulta> STATUS_ENCERRADOS = EnumSet.of(
            CONCLUIDA,
            CANCELADA_PELO_PACIENTE,
            CANCELADA_PELA_CLINICA,
            NAO_COMPARECEU
    );

    public boolean ocupaAgenda() {
        return STATUS_ATIVOS.contains(this);
    }

    public boolean encerrado() {
        return STATUS_ENCERRADOS.contains(this);
    }
}
