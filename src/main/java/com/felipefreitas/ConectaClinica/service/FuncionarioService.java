package com.felipefreitas.ConectaClinica.service;

import com.felipefreitas.ConectaClinica.dto.funcionario.FuncionarioRequestDTO;
import com.felipefreitas.ConectaClinica.dto.funcionario.FuncionarioResponseDTO;
import com.felipefreitas.ConectaClinica.entity.CargoEntity;
import com.felipefreitas.ConectaClinica.entity.EspecialidadeEntity;
import com.felipefreitas.ConectaClinica.entity.FuncionarioEntity;
import com.felipefreitas.ConectaClinica.enums.ErrorEnum;
import com.felipefreitas.ConectaClinica.enums.RoleFuncionario;
import com.felipefreitas.ConectaClinica.exceptions.BaseException;
import com.felipefreitas.ConectaClinica.repository.CargoRepository;
import com.felipefreitas.ConectaClinica.repository.EspecialidadeRepository;
import com.felipefreitas.ConectaClinica.repository.FuncionarioRepository;
import com.felipefreitas.ConectaClinica.util.CPFUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final EspecialidadeRepository especialidadeRepository;
    private final CargoRepository cargoRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @PreAuthorize("hasRole('GERENTE')")
    public FuncionarioResponseDTO cadastrarFuncionarioAdministrativo(FuncionarioRequestDTO funcionarioRequestDTO) {

        CargoEntity cargo = cargoRepository.findByNomeCargoIgnoreCase(funcionarioRequestDTO.nomeCargo())
                .orElseThrow(() -> new BaseException(ErrorEnum.CARGO_NAO_CADASTRADO));

        if (funcionarioRepository.findByEmail(funcionarioRequestDTO.email()).isPresent()) {
            throw new BaseException(ErrorEnum.EMAIL_FUNCIONARIO_JA_CADASTRADO);
        }

        String cpfLimpo = CPFUtil.cleanCpf(funcionarioRequestDTO.cpf());

        if (!CPFUtil.isValid(cpfLimpo)) {
            throw new BaseException(ErrorEnum.CPF_INVALIDO);
        }

        if (funcionarioRepository.findByCpf(cpfLimpo).isPresent()) {
            throw new BaseException(ErrorEnum.CPF_FUNCIONARIO_JA_CADASTRADO);
        }

        String senhaCriptografada = passwordEncoder.encode(funcionarioRequestDTO.senha());

        FuncionarioEntity funcionarioEntity = FuncionarioEntity.builder()
                .nome(funcionarioRequestDTO.nome())
                .cpf(cpfLimpo)
                .email(funcionarioRequestDTO.email())
                .senha(senhaCriptografada)
                .cargo(cargo)
                .role(funcionarioRequestDTO.role())
                .ativo(true)
                .build();

        FuncionarioEntity funcionarioSalvo = funcionarioRepository.save(funcionarioEntity);

        return new FuncionarioResponseDTO(funcionarioSalvo);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('GERENTE')")
    public FuncionarioResponseDTO buscarFuncionarioPorCpf(String cpf) {

        String cpfLimpo = CPFUtil.cleanCpf(cpf);

        if (!CPFUtil.isValid(cpfLimpo)) {
            throw new BaseException(ErrorEnum.CPF_INVALIDO);
        }

        FuncionarioEntity funcionario = funcionarioRepository.findByCpf(cpfLimpo)
                .orElseThrow(() -> new BaseException(ErrorEnum.FUNCIONARIO_NAO_ENCONTRADO));

        return new FuncionarioResponseDTO(funcionario);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('GERENTE')")
    public Page<FuncionarioResponseDTO> listarTodosFuncionarios(Pageable pageable) {
        return funcionarioRepository.findAll(pageable)
                .map(FuncionarioResponseDTO::new);
    }

    @Transactional
    @PreAuthorize("hasRole('GERENTE')")
    public void softDeleteFuncionario(Long id, FuncionarioEntity funcionarioAutenticado) {

        if (funcionarioAutenticado.getId().equals(id)) {
            throw new BaseException(ErrorEnum.ACAO_NAO_PERMITIDA);
        }

        FuncionarioEntity funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorEnum.FUNCIONARIO_NAO_ENCONTRADO));

        if (!funcionario.isAtivo()) {
            throw new BaseException(ErrorEnum.FUNCIONARIO_INATIVO);
        }

        funcionario.setAtivo(false);
    }

    @Transactional
    @PreAuthorize("hasRole('GERENTE')")
    public FuncionarioResponseDTO atualizarDadosFuncionario(Long id, FuncionarioRequestDTO funcionarioRequestDTO,
                                                            FuncionarioEntity funcionarioAutenticado) {

        FuncionarioEntity funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorEnum.FUNCIONARIO_NAO_ENCONTRADO));

        CargoEntity cargo = cargoRepository.findByNomeCargoIgnoreCase(funcionarioRequestDTO.nomeCargo())
                .orElseThrow(() -> new BaseException(ErrorEnum.CARGO_NAO_CADASTRADO));

        if (!funcionarioRequestDTO.email().equalsIgnoreCase(funcionario.getEmail())) {
            funcionarioRepository.findByEmail(funcionarioRequestDTO.email())
                    .ifPresent(f -> {
                        throw new BaseException(ErrorEnum.EMAIL_FUNCIONARIO_JA_CADASTRADO);
                    });
        }

        if (funcionarioAutenticado.getId().equals(id) &&
                (!funcionario.getCargo().getNome().equalsIgnoreCase(funcionarioRequestDTO.nomeCargo()) ||
                        !funcionario.getRole().equals(funcionarioRequestDTO.role()))) {
            throw new BaseException(ErrorEnum.ACAO_NAO_PERMITIDA);
        }

        funcionario.setNome(funcionarioRequestDTO.nome());
        funcionario.setEmail(funcionarioRequestDTO.email());
        funcionario.setCargo(cargo);
        funcionario.setRole(funcionarioRequestDTO.role());

        if (funcionarioRequestDTO.senha() != null && !funcionarioRequestDTO.senha().isBlank()) {
            funcionario.setSenha(passwordEncoder.encode(funcionarioRequestDTO.senha()));
        }
        return new FuncionarioResponseDTO(funcionario);
    }

    @Transactional
    @PreAuthorize("hasRole('GERENTE')")
    public FuncionarioResponseDTO cadastrarFuncionarioHospitalar(FuncionarioRequestDTO funcionarioRequestDTO) {

        CargoEntity cargo = cargoRepository.findByNomeCargoIgnoreCase(funcionarioRequestDTO.nomeCargo())
                .orElseThrow(() -> new BaseException(ErrorEnum.CARGO_NAO_CADASTRADO));

        EspecialidadeEntity especialidade =
                especialidadeRepository.findByNomeEspecialidadeIgnoreCase(funcionarioRequestDTO.nomeEspecialidade()).orElseThrow(() -> new BaseException(ErrorEnum.ESPECIALIDADE_NAO_ENCONTRADA));

        if (funcionarioRepository.findByEmail(funcionarioRequestDTO.email()).isPresent()) {
            throw new BaseException(ErrorEnum.EMAIL_FUNCIONARIO_JA_CADASTRADO);
        }

        String cpfLimpo = CPFUtil.cleanCpf(funcionarioRequestDTO.cpf());

        if (!CPFUtil.isValid(cpfLimpo)) {
            throw new BaseException(ErrorEnum.CPF_INVALIDO);
        }

        if (funcionarioRepository.findByCpf(cpfLimpo).isPresent()) {
            throw new BaseException(ErrorEnum.CPF_FUNCIONARIO_JA_CADASTRADO);
        }

        if (funcionarioRequestDTO.role() == RoleFuncionario.ROLE_MEDICO || funcionarioRequestDTO.role() == RoleFuncionario.ROLE_ENFERMAGEM) {

            if (funcionarioRequestDTO.registroProfissional() == null || funcionarioRequestDTO.registroProfissional().isBlank()) {
                throw new BaseException(ErrorEnum.REGISTRO_PROFISSIONAL_OBRIGATORIO);
            }

            funcionarioRepository.findByRegistroProfissional(funcionarioRequestDTO.registroProfissional())
                    .ifPresent(f -> {
                        throw new BaseException(ErrorEnum.REGISTRO_PROFISSIONAL_JA_CADASTRADO);
                    });
        }

        String senhaCriptografada = passwordEncoder.encode(funcionarioRequestDTO.senha());

        FuncionarioEntity funcionarioEntity = FuncionarioEntity.builder()
                .nome(funcionarioRequestDTO.nome())
                .cpf(cpfLimpo)
                .email(funcionarioRequestDTO.email())
                .senha(senhaCriptografada)
                .cargo(cargo)
                .role(funcionarioRequestDTO.role())
                .especialidade(especialidade)
                .registroProfissional(funcionarioRequestDTO.registroProfissional())
                .ativo(true)
                .build();

        FuncionarioEntity funcionarioSalvo = funcionarioRepository.save(funcionarioEntity);

        return new FuncionarioResponseDTO(funcionarioSalvo);
    }
}