package com.felipefreitas.ConectaClinica.dto.medico;

import com.felipefreitas.ConectaClinica.dto.especialidade.EspecialidadeResponseDTO;

public record MedicoResponseDTO(

        Long id,
        String nome,
        String crm,
        EspecialidadeResponseDTO especialidade,
        String email,
        String telefone,
        boolean ativo

) {
}
