package com.felipefreitas.ConectaClinica.dto.agendamento;

import com.felipefreitas.ConectaClinica.enums.TipoCancelamentoConsulta;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Solicitação de cancelamento de consulta.")
public record ConsultaCancelamentoRequestDTO(
        @NotNull(message = "Tipo de cancelamento é obrigatório")
        TipoCancelamentoConsulta tipo,

        @NotBlank(message = "Motivo do cancelamento é obrigatório")
        String motivo
) {
}
