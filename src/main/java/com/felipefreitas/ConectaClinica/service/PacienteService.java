package com.felipefreitas.ConectaClinica.service;

import com.felipefreitas.ConectaClinica.dto.agendamento.PacienteRequestDTO;
import com.felipefreitas.ConectaClinica.dto.agendamento.PacienteResponseDTO;
import com.felipefreitas.ConectaClinica.entity.PacienteEntity;
import com.felipefreitas.ConectaClinica.enums.ErrorEnum;
import com.felipefreitas.ConectaClinica.exceptions.BaseExceptions;
import com.felipefreitas.ConectaClinica.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final AgendamentoAutorizacaoService autorizacaoService;

    @Transactional
    public PacienteResponseDTO cadastrar(PacienteRequestDTO request, Authentication authentication) {
        autorizacaoService.exigirGestaoAgenda(authentication);

        var paciente = PacienteEntity.builder()
                .nome(request.nome().trim())
                .build();

        return toResponse(pacienteRepository.save(paciente));
    }

    @Transactional(readOnly = true)
    public List<PacienteResponseDTO> listar(Authentication authentication) {
        autorizacaoService.exigirUsuarioAutenticado(authentication);
        return pacienteRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PacienteResponseDTO buscar(UUID id, Authentication authentication) {
        autorizacaoService.exigirUsuarioAutenticado(authentication);
        return toResponse(pacienteRepository.findById(id)
                .orElseThrow(() -> new BaseExceptions(ErrorEnum.PACIENTE_NAO_ENCONTRADO)));
    }

    private PacienteResponseDTO toResponse(PacienteEntity paciente) {
        return new PacienteResponseDTO(paciente.getId(), paciente.getNome());
    }
}
