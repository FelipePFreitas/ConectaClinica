package com.felipefreitas.ConectaClinica.dto.especialidade;

import com.felipefreitas.ConectaClinica.entity.EspecialidadeEntity;

public record EspecialidadeResponseDTO(
        Long id,
        String especialidade) {

    public EspecialidadeResponseDTO(EspecialidadeEntity especialidade) {
        this(
                especialidade.getId(),
                especialidade.getNome()
        );
    }
}
