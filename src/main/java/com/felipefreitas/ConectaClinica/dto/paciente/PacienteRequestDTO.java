package com.felipefreitas.ConectaClinica.dto.paciente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

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
        @Past(message = "A data de nascimento deve ser no passado")
        LocalDate dataNascimento

) {
}
