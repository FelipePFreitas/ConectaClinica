package com.felipefreitas.ConectaClinica.service;

import com.felipefreitas.ConectaClinica.dto.especialidade.EspecialidadeRequestDTO;
import com.felipefreitas.ConectaClinica.dto.especialidade.EspecialidadeResponseDTO;
import com.felipefreitas.ConectaClinica.entity.EspecialidadeEntity;
import com.felipefreitas.ConectaClinica.enums.ErrorEnum;
import com.felipefreitas.ConectaClinica.exceptions.BaseException;
import com.felipefreitas.ConectaClinica.repository.EspecialidadeRepository;
import com.felipefreitas.ConectaClinica.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspecialidadeService {

    private final EspecialidadeRepository especialidadeRepository;
    private final FuncionarioRepository funcionarioRepository;


    @Transactional
    @PreAuthorize("hasRole('GERENTE')")
    public EspecialidadeResponseDTO cadastrarEspecialidade(EspecialidadeRequestDTO especialidadeRequestDTO) {

        if (especialidadeRepository.findByNomeEspecialidadeIgnoreCase(especialidadeRequestDTO.nome()).isPresent()) {
            throw new BaseException(ErrorEnum.ESPECIALIDADE_JA_CADASTRADA);
        }

        EspecialidadeEntity especialidade = EspecialidadeEntity.builder()
                .nome(especialidadeRequestDTO.nome())
                .build();

        EspecialidadeEntity especialidadeSalvo = especialidadeRepository.save(especialidade);

        return new EspecialidadeResponseDTO(especialidadeSalvo);

    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('GERENTE')")
    public List<EspecialidadeResponseDTO> listarEspecialidade() {

        return especialidadeRepository.findAll(Sort.by(Sort.Direction.ASC, "nome")).stream().map(EspecialidadeResponseDTO::new).toList();

    }


    @Transactional
    @PreAuthorize("hasRole('GERENTE')")
    public void deletarEspecialidadePorId(Long id) {

        EspecialidadeEntity cargo =
                especialidadeRepository.findById(id).orElseThrow(() -> new BaseException(ErrorEnum.ESPECIALIDADE_NAO_ENCONTRADA));

        if (funcionarioRepository.existsByCargoId(id)) {
            throw new BaseException(ErrorEnum.ESPECIALIDADE_POSSUI_MEDICOS_VINCULADOS);
        }

        especialidadeRepository.delete(cargo);

    }


}
