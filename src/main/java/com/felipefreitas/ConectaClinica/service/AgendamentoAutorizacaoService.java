package com.felipefreitas.ConectaClinica.service;

import com.felipefreitas.ConectaClinica.enums.ErrorEnum;
import com.felipefreitas.ConectaClinica.exceptions.BaseExceptions;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AgendamentoAutorizacaoService {

    private static final Set<String> PERFIS_GESTAO_AGENDA = Set.of(
            "ADMIN",
            "FUNCIONARIO",
            "PROFISSIONAL"
    );

    public String exigirUsuarioAutenticado(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new BaseExceptions(ErrorEnum.PERMISSAO_AGENDAMENTO_NEGADA);
        }

        return authentication.getName();
    }

    public String exigirGestaoAgenda(Authentication authentication) {
        String usuario = exigirUsuarioAutenticado(authentication);
        boolean autorizado = authentication.getAuthorities()
                .stream()
                .anyMatch(authority -> {
                    String nome = authority.getAuthority();
                    String nomeSemPrefixo = nome.startsWith("ROLE_") ? nome.substring(5) : nome;
                    return PERFIS_GESTAO_AGENDA.contains(nomeSemPrefixo);
                });

        if (!autorizado) {
            throw new BaseExceptions(ErrorEnum.PERMISSAO_AGENDAMENTO_NEGADA);
        }

        return usuario;
    }
}
