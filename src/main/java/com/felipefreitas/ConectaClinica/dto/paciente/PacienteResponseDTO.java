package com.felipefreitas.ConectaClinica.dto.paciente;

import com.felipefreitas.ConectaClinica.entity.FuncionarioEntity;
import com.felipefreitas.ConectaClinica.entity.PacienteEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PacienteResponseDTO(

        Long id,
        String nome,
        String cpf,
        String email,
        String telefone,
        LocalDate dataNascimento,
        LocalDateTime dataCadastro,
        String endereco,
        String numero,
        String bairro,
        String cidade,
        String estado

) {
    public PacienteResponseDTO(PacienteEntity pacienteEntity) {
        this(
                pacienteEntity.getId(),
                pacienteEntity.getNome(),
                pacienteEntity.getCpf(),
                pacienteEntity.getEmail(),
                pacienteEntity.getTelefone(),
                pacienteEntity.getDataNascimento(),
                pacienteEntity.getDataCadastro(),
                pacienteEntity.getEndereco(),
                pacienteEntity.getNumero(),
                pacienteEntity.getBairro(),
                pacienteEntity.getCidade(),
                pacienteEntity.getEstado()

        );
    }
}
