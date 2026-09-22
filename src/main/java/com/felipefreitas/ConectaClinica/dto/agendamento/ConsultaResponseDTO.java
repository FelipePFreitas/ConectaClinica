package com.felipefreitas.ConectaClinica.dto.agendamento;

import com.felipefreitas.ConectaClinica.enums.ModalidadeConsulta;
import com.felipefreitas.ConectaClinica.enums.StatusConsulta;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Consulta retornada pela API sem expor entidades de domínio.")
public record ConsultaResponseDTO(
        UUID id,
        PacienteResumoDTO paciente,
        ProfissionalResumoDTO profissional,
        OffsetDateTime inicio,
        OffsetDateTime fim,
        Integer duracaoMinutos,
        ModalidadeConsulta modalidade,
        String localOuCanal,
        StatusConsulta status,
        String criadoPor,
        OffsetDateTime criadoEm,
        String atualizadoPor,
        OffsetDateTime atualizadoEm,
        String canceladoPor,
        OffsetDateTime canceladoEm,
        String motivoCancelamento
) {
}
