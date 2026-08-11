package com.felipefreitas.ConectaClinica.service;

import com.felipefreitas.ConectaClinica.dto.paciente.PacienteRequestDTO;
import com.felipefreitas.ConectaClinica.dto.paciente.PacienteResponseDTO;
import com.felipefreitas.ConectaClinica.entity.PacienteEntity;
import com.felipefreitas.ConectaClinica.enums.ErrorEnum;
import com.felipefreitas.ConectaClinica.exceptions.BaseException;
import com.felipefreitas.ConectaClinica.repository.PacienteRepository;
import com.felipefreitas.ConectaClinica.util.CPFUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;


    @Transactional
    public PacienteResponseDTO cadastrarPaciente (PacienteRequestDTO pacienteRequestDTO){

        String cpfLimpo = CPFUtil.cleanCpf(pacienteRequestDTO.cpf());

        if (!CPFUtil.isValid(cpfLimpo)){
            throw new BaseException(ErrorEnum.CPF_INVALIDO);
        }

        if (pacienteRepository.existsByCpf(cpfLimpo)){
            throw new BaseException(ErrorEnum.CPF_PACIENTE_JA_CADASTRADO);
        }

        String emailNormalizado = pacienteRequestDTO.email().toLowerCase().trim();

        if (pacienteRepository.existsByEmail(emailNormalizado)){
            throw new BaseException(ErrorEnum.EMAIL_PACIENTE_JA_CADASTRADO);
        }

        PacienteEntity paciente = PacienteEntity.builder()
                .nome(pacienteRequestDTO.nome())
                .cpf(cpfLimpo)
                .email(emailNormalizado)
                .telefone(pacienteRequestDTO.telefone())
                .dataNascimento(pacienteRequestDTO.dataNascimento())
                .dataCadastro(LocalDateTime.now())
                .endereco(pacienteRequestDTO.endereco())
                .numero(pacienteRequestDTO.numero())
                .bairro(pacienteRequestDTO.bairro())
                .cidade(pacienteRequestDTO.cidade())
                .estado(pacienteRequestDTO.estado())
                .build();

        PacienteEntity pacienteSalvo = pacienteRepository.save(paciente);

        return new PacienteResponseDTO(pacienteSalvo);
    }
}
