package com.felipefreitas.ConectaClinica.service;

import com.felipefreitas.ConectaClinica.dto.agendamento.ProfissionalRequestDTO;
import com.felipefreitas.ConectaClinica.dto.agendamento.ProfissionalResponseDTO;
import com.felipefreitas.ConectaClinica.entity.ProfissionalEntity;
import com.felipefreitas.ConectaClinica.enums.ErrorEnum;
import com.felipefreitas.ConectaClinica.exceptions.BaseExceptions;
import com.felipefreitas.ConectaClinica.repository.ProfissionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;
    private final AgendamentoAutorizacaoService autorizacaoService;

    @Transactional
    public ProfissionalResponseDTO cadastrar(ProfissionalRequestDTO request, Authentication authentication) {
        autorizacaoService.exigirGestaoAgenda(authentication);

        var profissional = ProfissionalEntity.builder()
                .nome(request.nome().trim())
                .ativo(request.ativo() == null || request.ativo())
                .build();

        return toResponse(profissionalRepository.save(profissional));
    }

    @Transactional(readOnly = true)
    public List<ProfissionalResponseDTO> listar(Authentication authentication) {
        autorizacaoService.exigirUsuarioAutenticado(authentication);
        return profissionalRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProfissionalResponseDTO buscar(UUID id, Authentication authentication) {
        autorizacaoService.exigirUsuarioAutenticado(authentication);
        return toResponse(profissionalRepository.findById(id)
                .orElseThrow(() -> new BaseExceptions(ErrorEnum.PROFISSIONAL_NAO_ENCONTRADO)));
    }

    private ProfissionalResponseDTO toResponse(ProfissionalEntity profissional) {
        return new ProfissionalResponseDTO(
                profissional.getId(),
                profissional.getNome(),
                profissional.isAtivo()
        );
    }
}
