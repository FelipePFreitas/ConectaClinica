package com.felipefreitas.ConectaClinica.dto.agendamento;

import com.felipefreitas.ConectaClinica.enums.StatusConsulta;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Solicitação de transição de status da consulta.")
public record ConsultaStatusRequestDTO(
        @NotNull(message = "Status da consulta é obrigatório")
        StatusConsulta status,

        @Schema(description = "Motivo obrigatório para cancelamentos ou mudanças sensíveis.")
        String motivo
) {
}
