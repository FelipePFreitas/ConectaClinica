package com.felipefreitas.ConectaClinica.dto.consulta;

import com.felipefreitas.ConectaClinica.dto.especialidade.EspecialidadeResponseDTO;
import com.felipefreitas.ConectaClinica.dto.funcionario.FuncionarioResponseDTO;
import com.felipefreitas.ConectaClinica.dto.medico.MedicoResponseDTO;
import com.felipefreitas.ConectaClinica.dto.paciente.PacienteResponseDTO;
import com.felipefreitas.ConectaClinica.dto.tipoexame.TipoExameResponseDTO;
import com.felipefreitas.ConectaClinica.enums.StatusAgendamento;

import java.time.LocalDateTime;

public record ConsultaResponseDTO(
        Long id,
        PacienteResponseDTO paciente,
        EspecialidadeResponseDTO especialidade,
        TipoExameResponseDTO tipoExame,
        MedicoResponseDTO medico,
        String observacao,
        LocalDateTime dataCadastro,
        StatusAgendamento statusAgendamento,
        LocalDateTime dataConsulta,
        FuncionarioResponseDTO funcionario
) {
}
