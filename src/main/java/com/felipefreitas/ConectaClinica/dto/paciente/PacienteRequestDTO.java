package com.felipefreitas.ConectaClinica.dto.paciente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PacienteRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "Nome não pode passar de 100 caracteres")
        String nome,

        @NotBlank(message = "O cpf é obrigatório")
        @CPF(message = "O formato de cpf é inválido")
        String cpf,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O formato de email é inválido")
        String email,

        @NotBlank(message = "O telefone é obrigatório")
        @Size(max = 20, message = "O telefone não pode passar de 20 caracteres")
        String telefone,

        @NotBlank(message = "Data de nascimento é obrigatório")
        LocalDate dataNascimento,

        @CreationTimestamp
        @NotBlank(message = "Data do cadastro é obrigatório")
        LocalDateTime dataCadastro,

        @NotBlank(message = "Endereço é obrigatório")
        @Size(max = 50, message = "Não pode passar de 50 caracteres")
        String endereco,

        @NotBlank(message = "Número é obrigatório")
        @Size(max = 20, message = "Não pode passar de 20 caracteres")
        String numero,

        @NotBlank(message = "Bairro é obrigatório")
        @Size(max = 50, message = "Não pode passar de 50 caracteres")
        String bairro,

        @NotBlank(message = "Cidade é obrigatório")
        @Size(max = 50, message = "Não pode passar de 50 caracteres")
        String cidade,

        @NotBlank(message = "Estado é obrigatório")
        @Size(max = 2, message = "Não pode passar de 2 caracteres")
        String estado

) {
}
