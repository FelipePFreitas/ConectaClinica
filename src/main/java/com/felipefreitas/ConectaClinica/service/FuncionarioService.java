package com.felipefreitas.ConectaClinica.service;

import com.felipefreitas.ConectaClinica.dto.funcionario.FuncionarioRequestDTO;
import com.felipefreitas.ConectaClinica.dto.funcionario.FuncionarioResponseDTO;
import com.felipefreitas.ConectaClinica.entity.FuncionarioEntity;
import com.felipefreitas.ConectaClinica.entity.RoleUsuarioEntity;
import com.felipefreitas.ConectaClinica.entity.UsuarioEntity;
import com.felipefreitas.ConectaClinica.enums.ErrorEnum;
import com.felipefreitas.ConectaClinica.exceptions.BaseExceptions;
import com.felipefreitas.ConectaClinica.repository.FuncionarioRepository;
import com.felipefreitas.ConectaClinica.repository.RoleUsuarioRepository;
import com.felipefreitas.ConectaClinica.repository.UsuarioRepository;
import com.felipefreitas.ConectaClinica.util.SenhaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FuncionarioService {


    private final FuncionarioRepository funcionarioRepository;
    private final RoleUsuarioRepository roleUsuarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public FuncionarioResponseDTO cadastrarFuncionario(FuncionarioRequestDTO funcionarioRequestDTO) {

        if (funcionarioRepository.existsByCpf(funcionarioRequestDTO.cpf())) {
            throw new BaseExceptions(ErrorEnum.CPF_JA_CADASTRADO);
        }

        if (funcionarioRepository.existsByEmail(funcionarioRequestDTO.email())) {
            throw new BaseExceptions(ErrorEnum.EMAIL_JA_CADASTRADO);
        }

        FuncionarioEntity funcionarioEntity = FuncionarioEntity.builder()
                .nome(funcionarioRequestDTO.nome())
                .cpf(funcionarioRequestDTO.cpf())
                .email(funcionarioRequestDTO.email())
                .build();

        FuncionarioEntity funcionarioSalvo = funcionarioRepository.save(funcionarioEntity);

        String senhaAleatoria = SenhaUtil.geradorSenhaAleatoria(10);

        var roleUsuario = roleUsuarioRepository.findByNome("ROLE_USER")
                .orElseGet(() -> roleUsuarioRepository.save(
                        RoleUsuarioEntity.builder().nome("ROLE_USER").build()
                ));

        UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .login(funcionarioSalvo.getCpf())
                .senha(passwordEncoder.encode(senhaAleatoria))
                .funcionario(funcionarioSalvo)
                .role(roleUsuario)
                .build();

        usuarioRepository.save(usuarioEntity);

        emailService.enviarSenhaInicial(
                funcionarioSalvo.getNome(),
                funcionarioSalvo.getEmail(),
                funcionarioSalvo.getCpf(),
                senhaAleatoria
        );

        return new FuncionarioResponseDTO(
                funcionarioSalvo.getId(),
                funcionarioSalvo.getNome(),
                funcionarioSalvo.getCpf(),
                funcionarioSalvo.getEmail());
    }
}
