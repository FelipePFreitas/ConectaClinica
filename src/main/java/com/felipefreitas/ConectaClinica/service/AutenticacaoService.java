package com.felipefreitas.ConectaClinica.service;

import com.felipefreitas.ConectaClinica.dto.login.LoginRequestDTO;
import com.felipefreitas.ConectaClinica.dto.login.LoginResponseDTO;
import com.felipefreitas.ConectaClinica.entity.FuncionarioEntity;
import com.felipefreitas.ConectaClinica.enums.ErrorEnum;
import com.felipefreitas.ConectaClinica.exceptions.BaseException;
import com.felipefreitas.ConectaClinica.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticacaoService {

    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponseDTO autenticar(LoginRequestDTO loginRequestDTO) {

        // 1. Buscar usuário pelo e-mail
        FuncionarioEntity funcionarioEntity = funcionarioRepository.findByEmail(loginRequestDTO.email())
                .orElseThrow(() -> new BaseException(ErrorEnum.CREDENCIAIS_INVALIDAS));

        if (!funcionarioEntity.isAtivo()) {
            throw new BaseException(ErrorEnum.FUNCIONARIO_INATIVO);
        }

        // 2. Validar se a senha enviada bate com o hash salvo no banco
        if (!passwordEncoder.matches(loginRequestDTO.senha(), funcionarioEntity.getSenha())) {
            throw new BaseException(ErrorEnum.CREDENCIAIS_INVALIDAS);
        }

        // 3. Gerar o token JWT para o e-mail autenticado
        String token = jwtService.gerarToken(funcionarioEntity.getEmail());

        // 4. Retornar a resposta formatada
        return new LoginResponseDTO(
                token,
                "Bearer",
                28800000L // Tempo de expiração em ms (8 horas)
        );
    }
}