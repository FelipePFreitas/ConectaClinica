package com.felipefreitas.ConectaClinica.dto.cargo;

import com.felipefreitas.ConectaClinica.entity.CargoEntity;

public record CargoResponseDTO(
        Long id,
        String cargo
) {

    public CargoResponseDTO(CargoEntity cargoEntity) {
            this(
                    cargoEntity.getId(),
                    cargoEntity.getNome()
            );
        }
}
