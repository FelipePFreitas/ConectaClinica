package com.felipefreitas.ConectaClinica.service;

import com.felipefreitas.ConectaClinica.dto.funcionario.FuncionarioRequestDTO;
import com.felipefreitas.ConectaClinica.dto.funcionario.FuncionarioResponseDTO;
import com.felipefreitas.ConectaClinica.entity.FuncionarioEntity;
import com.felipefreitas.ConectaClinica.entity.UsuarioEntity;
import com.felipefreitas.ConectaClinica.enums.ErrorEnum;
import com.felipefreitas.ConectaClinica.enums.RoleUsuario;
import com.felipefreitas.ConectaClinica.exceptions.BaseExceptions;
import com.felipefreitas.ConectaClinica.repository.FuncionarioRepository;
import com.felipefreitas.ConectaClinica.repository.UsuarioRepository;
import com.felipefreitas.ConectaClinica.util.SenhaUtil;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Server
@AllArgsConstructor
public class FucionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public FuncionarioResponseDTO cadastrarFuncionario(FuncionarioRequestDTO funcionarioRequestDTO) {

        if (funcionarioRepository.existsByCpf(funcionarioRequestDTO.cpf())) {
            throw new BaseExceptions(ErrorEnum.CPF_JA_CADASTRADO);
        }

        FuncionarioEntity funcionarioEntity = FuncionarioEntity.builder()
                .nome(funcionarioRequestDTO.nome())
                .cpf(funcionarioRequestDTO.cpf())
                .build();

        FuncionarioEntity funcionarioSalvo = funcionarioRepository.save(funcionarioEntity);

        String senhaAleatoria = SenhaUtil.geradorSenhaAleatoria(10);

        UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .login(funcionarioSalvo.getCpf())
                .senha(senhaAleatoria)
                .funcionario(funcionarioSalvo)
                .role(RoleUsuario.ROLE_USER)
                .build();

        usuarioRepository.save(usuarioEntity);

        return new FuncionarioResponseDTO(
                funcionarioSalvo.getId(),
                funcionarioSalvo.getNome(),
                funcionarioSalvo.getCpf());
    }
}
