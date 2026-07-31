package com.felipefreitas.ConectaClinica.dto.consulta;

import com.felipefreitas.ConectaClinica.enums.StatusAgendamento;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ConsultaRequestDTO(

        @NotNull(message = "O paciente é obrigatório")
        Long idPaciente,

        @NotNull(message = "A especialidade é obrigatória")
        Long idEspecialidade,

        @NotNull(message = " Tipo de exame é obrigatório")
        Long idTipoExame,

        @NotNull(message = "O médico é obrigatório")
        Long idMedico,

        @NotNull(message = "O funcionário é obrigatório")
        Long idFuncionario,

        @NotBlank(message = "A observação é obrigatória")
        @Size(max = 100, message = "A observação deve ter no máximo 100 caracteres")
        String observacao,

        @NotNull(message = "A data de cadastro é obrigatório")
        @FutureOrPresent(message = "A data de cadastro deve ser hoje ou futura")
        LocalDateTime dataCadastro,

        @NotNull(message = "A data da consulta não pode ser nula")
        LocalDateTime dataConsulta,

        @NotNull(message = "O status do agendamento é obrigatório")
        StatusAgendamento statusAgendamento

) {

}
