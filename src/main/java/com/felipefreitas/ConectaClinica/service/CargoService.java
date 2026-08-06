package com.felipefreitas.ConectaClinica.service;

import com.felipefreitas.ConectaClinica.dto.cargo.CargoRequestDTO;
import com.felipefreitas.ConectaClinica.dto.cargo.CargoResponseDTO;
import com.felipefreitas.ConectaClinica.entity.CargoEntity;
import com.felipefreitas.ConectaClinica.enums.ErrorEnum;
import com.felipefreitas.ConectaClinica.exceptions.BaseException;
import com.felipefreitas.ConectaClinica.repository.CargoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CargoService {

    private final CargoRepository cargoRepository;


    @Transactional
    @PreAuthorize("hasRole('GERENTE')")
    public CargoResponseDTO cadastrarCargo(CargoRequestDTO cargoRequestDTO) {

        if (cargoRepository.findByNomeCargoIgnoreCase(cargoRequestDTO.nome()).isPresent()) {
            throw new BaseException(ErrorEnum.CARGO_JA_CADASTRADO);
        }

        CargoEntity cargo = CargoEntity.builder()
                .nome(cargoRequestDTO.nome())
                .build();

        CargoEntity cargoSalvo = cargoRepository.save(cargo);

        return new CargoResponseDTO(cargoSalvo);

    }

}
