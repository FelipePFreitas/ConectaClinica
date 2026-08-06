package com.felipefreitas.ConectaClinica.service;

import com.felipefreitas.ConectaClinica.dto.cargo.CargoRequestDTO;
import com.felipefreitas.ConectaClinica.dto.cargo.CargoResponseDTO;
import com.felipefreitas.ConectaClinica.entity.CargoEntity;
import com.felipefreitas.ConectaClinica.enums.ErrorEnum;
import com.felipefreitas.ConectaClinica.exceptions.BaseException;
import com.felipefreitas.ConectaClinica.repository.CargoRepository;
import com.felipefreitas.ConectaClinica.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CargoService {

    private final CargoRepository cargoRepository;
    private final FuncionarioRepository funcionarioRepository;


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

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('GERENTE')")
    public List<CargoResponseDTO> listarCargos() {

        return cargoRepository.findAll(Sort.by(Sort.Direction.ASC, "nome")).stream().map(CargoResponseDTO::new).toList();

    }


    @Transactional
    @PreAuthorize("hasRole('GERENTE')")
    public void deletarCargoPorId(Long id) {

        CargoEntity cargo =
                cargoRepository.findById(id).orElseThrow(() -> new BaseException(ErrorEnum.CARGO_NAO_CADASTRADO));

        if(funcionarioRepository.existsByCargoId(id)){
            throw new BaseException(ErrorEnum.CARGO_POSSUI_FUNCIONARIOS_VINCULADOS);
        }

        cargoRepository.delete(cargo);

    }

}
