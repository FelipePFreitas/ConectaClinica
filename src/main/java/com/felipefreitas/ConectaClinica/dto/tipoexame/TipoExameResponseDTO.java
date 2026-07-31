package com.felipefreitas.ConectaClinica.dto.tipoexame;

import com.felipefreitas.ConectaClinica.dto.especialidade.EspecialidadeResponseDTO;

public record TipoExameResponseDTO(

        Long id,
        String exame,
        String descricao,
        EspecialidadeResponseDTO especialidade,
        Integer duracao,
        boolean ativo

) {
}
