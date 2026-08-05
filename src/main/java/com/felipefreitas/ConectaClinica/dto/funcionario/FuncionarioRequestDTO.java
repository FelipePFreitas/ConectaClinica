package com.felipefreitas.ConectaClinica.dto.funcionario;

import com.felipefreitas.ConectaClinica.enums.RoleFuncionario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record FuncionarioRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "O CPF é obrigatório")
        @CPF(message = "CPF inválido")
        String cpf,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String senha,

        @NotNull(message = "O ID do cargo é obrigatório")
        String nomeCargo,

        @NotNull(message = "A role é obrigatória")
        RoleFuncionario role,

        // Campos específicos para médicos (validados condicionalmente no Service)
        String registroProfissional,

        String nomeEspecialidade
) {}