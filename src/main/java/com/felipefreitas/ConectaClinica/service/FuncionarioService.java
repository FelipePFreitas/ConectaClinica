package com.felipefreitas.ConectaClinica.service;

import com.felipefreitas.ConectaClinica.util.CPFUtil;
import com.felipefreitas.ConectaClinica.dto.funcionario.FuncionarioRequestDTO;
import com.felipefreitas.ConectaClinica.dto.funcionario.FuncionarioResponseDTO;
import com.felipefreitas.ConectaClinica.entity.FuncionarioEntity;
import com.felipefreitas.ConectaClinica.enums.Cargo;
import com.felipefreitas.ConectaClinica.enums.ErrorEnum;
import com.felipefreitas.ConectaClinica.enums.RoleFuncionario;
import com.felipefreitas.ConectaClinica.exceptions.BaseException;
import com.felipefreitas.ConectaClinica.repository.FuncionarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public FuncionarioResponseDTO cadastrarFuncionario(FuncionarioRequestDTO funcionarioRequestDTO) {


        if (funcionarioRepository.findByEmail(funcionarioRequestDTO.email()).isPresent()) {
            throw new BaseException(ErrorEnum.EMAIL_FUNCIONARIO_JA_CADASTRADO);
        }

        if (!CPFUtil.isValid(funcionarioRequestDTO.cpf())) {
            throw new BaseException(ErrorEnum.CPF_INVALIDO);
        }

        if (funcionarioRepository.findByCpf(funcionarioRequestDTO.cpf()).isPresent()) {
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
                .cpf(funcionarioRequestDTO.cpf())
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


}
