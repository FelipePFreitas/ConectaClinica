package com.felipefreitas.ConectaClinica.service;

import com.felipefreitas.ConectaClinica.dto.funcionario.FuncionarioRequestDTO;
import com.felipefreitas.ConectaClinica.dto.funcionario.FuncionarioResponseDTO;
import com.felipefreitas.ConectaClinica.entity.FuncionarioEntity;
import com.felipefreitas.ConectaClinica.enums.Cargo;
import com.felipefreitas.ConectaClinica.enums.ErrorEnum;
import com.felipefreitas.ConectaClinica.enums.RoleFuncionario;
import com.felipefreitas.ConectaClinica.exceptions.BaseException;
import com.felipefreitas.ConectaClinica.repository.FuncionarioRepository;
import com.felipefreitas.ConectaClinica.util.CPFUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @PreAuthorize("hasRole('GERENTE')")
    public FuncionarioResponseDTO cadastrarFuncionario(FuncionarioRequestDTO funcionarioRequestDTO) {

        if (funcionarioRepository.findByEmail(funcionarioRequestDTO.email()).isPresent()) {
            throw new BaseException(ErrorEnum.EMAIL_FUNCIONARIO_JA_CADASTRADO);
        }
        String cpfLimpo = CPFUtil.cleanCpf(funcionarioRequestDTO.cpf());

        if (!CPFUtil.isValid(funcionarioRequestDTO.cpf())) {
            throw new BaseException(ErrorEnum.CPF_INVALIDO);
        }

        if (funcionarioRepository.findByCpf(cpfLimpo).isPresent()) {
            throw new BaseException(ErrorEnum.CPF_FUNCIONARIO_JA_CADASTRADO);
        }

        if (funcionarioRequestDTO.cargo() == null) {
            throw new BaseException(ErrorEnum.DADOS_INVALIDOS);
        }

        boolean cargoValido = Arrays.stream(Cargo.values())
                .anyMatch(c -> c.name().equalsIgnoreCase(String.valueOf(funcionarioRequestDTO.cargo())));

        if (!cargoValido) {
            throw new BaseException(ErrorEnum.CARGO_NAO_CADASTRADO);
        }

        boolean roleValida =
                Arrays.stream(RoleFuncionario.values()).anyMatch(c -> c.name().equalsIgnoreCase(String.valueOf(funcionarioRequestDTO.role())));

        if (!roleValida) {
            throw new BaseException(ErrorEnum.ROLE_NAO_CADASTRADO);
        }

        String senhaCriptografada = passwordEncoder.encode(funcionarioRequestDTO.senha());

        FuncionarioEntity funcionarioEntity = FuncionarioEntity.builder()
                .nome(funcionarioRequestDTO.nome())
                .cpf(cpfLimpo)
                .email(funcionarioRequestDTO.email())
                .senha(senhaCriptografada)
                .cargo(funcionarioRequestDTO.cargo())
                .role(funcionarioRequestDTO.role())
                .ativo(true)
                .build();

        FuncionarioEntity funcionarioSalvo = funcionarioRepository.save(funcionarioEntity);

        return new FuncionarioResponseDTO(funcionarioSalvo.getId(),
                funcionarioSalvo.getNome(),
                funcionarioSalvo.getCpf(),
                funcionarioSalvo.getEmail(),
                funcionarioSalvo.getCargo(),
                funcionarioSalvo.getRole(),
                funcionarioSalvo.isAtivo());
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('GERENTE')")
    public FuncionarioResponseDTO buscarFuncionarioPorCpf(String cpf) {

        if (!CPFUtil.isValid(cpf)) {
            throw new BaseException(ErrorEnum.CPF_INVALIDO);
        }

        String cpfLimpo = CPFUtil.cleanCpf(cpf);

        FuncionarioEntity funcionario =
                funcionarioRepository.findByCpf(cpfLimpo).orElseThrow(() -> new BaseException(ErrorEnum.FUNCIONARIO_NAO_ENCONTRADO));


        return new FuncionarioResponseDTO(funcionario.getId(),
                funcionario.getNome(),
                funcionario.getCpf(),
                funcionario.getEmail(),
                funcionario.getCargo(),
                funcionario.getRole(),
                funcionario.isAtivo());
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('GERENTE')")
    public Page<FuncionarioResponseDTO> listarTodosFuncionarios(Pageable pageable) {
        return funcionarioRepository.findAll(pageable)
                .map(funcionario -> new FuncionarioResponseDTO(
                        funcionario.getId(),
                        funcionario.getNome(),
                        funcionario.getCpf(),
                        funcionario.getEmail(),
                        funcionario.getCargo(),
                        funcionario.getRole(),
                        funcionario.isAtivo()
                ));
    }


    @Transactional
    @PreAuthorize("hasRole('GERENTE')")
    public void softDeleteFuncionario(Long id, FuncionarioEntity funcionarioAutenticado) {

        if (funcionarioAutenticado.getId().equals(id)) {
            throw new BaseException(ErrorEnum.ACAO_NAO_PERMITIDA);
        }

        FuncionarioEntity funcionario =
                funcionarioRepository.findById(id).orElseThrow(() -> new BaseException(ErrorEnum.FUNCIONARIO_NAO_ENCONTRADO));

        if (!funcionario.isAtivo()) {
            throw new BaseException(ErrorEnum.FUNCIONARIO_INATIVO);
        }

        funcionario.setAtivo(false);

        funcionarioRepository.save(funcionario);

    }

    @Transactional
    @PreAuthorize("hasRole('GERENTE')")
    public FuncionarioResponseDTO atualizarDadosFuncionario(Long id, FuncionarioRequestDTO funcionarioRequestDTO,
                                                            FuncionarioEntity funcionarioAutenticado) {

        FuncionarioEntity funcionario =
                funcionarioRepository.findById(id).orElseThrow(() -> new BaseException(ErrorEnum.FUNCIONARIO_NAO_ENCONTRADO));

        if (!funcionarioRequestDTO.email().equalsIgnoreCase(funcionario.getEmail())) {
            funcionarioRepository.findByEmail(funcionarioRequestDTO.email())
                    .ifPresent(f -> {
                        throw new BaseException(ErrorEnum.EMAIL_FUNCIONARIO_JA_CADASTRADO);
                    });
        }

        if (funcionarioAutenticado.getId().equals(id) &&
                (!funcionario.getCargo().equals(funcionarioRequestDTO.cargo()) ||
                        !funcionario.getRole().equals(funcionarioRequestDTO.role()))) {
            throw new BaseException(ErrorEnum.ACAO_NAO_PERMITIDA);
        }

        funcionario.setNome(funcionarioRequestDTO.nome());
        funcionario.setEmail(funcionarioRequestDTO.email());
        funcionario.setCargo(funcionarioRequestDTO.cargo());
        funcionario.setRole(funcionarioRequestDTO.role());

        if (funcionarioRequestDTO.senha() == null || !funcionarioRequestDTO.senha().isBlank()) {
            funcionario.setSenha(passwordEncoder.encode(funcionarioRequestDTO.senha()));
        }

        FuncionarioEntity funcionarioAtualizado = funcionarioRepository.save(funcionario);

        return new FuncionarioResponseDTO(funcionarioAtualizado.getId(),
                funcionarioAtualizado.getNome(),
                funcionarioAtualizado.getCpf(),
                funcionarioAtualizado.getEmail(),
                funcionarioAtualizado.getCargo(),
                funcionarioAtualizado.getRole(),
                funcionarioAtualizado.isAtivo());
    }


}
