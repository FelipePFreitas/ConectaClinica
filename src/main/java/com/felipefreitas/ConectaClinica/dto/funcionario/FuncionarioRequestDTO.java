package com.felipefreitas.ConectaClinica.dto.funcionario;

import com.felipefreitas.ConectaClinica.enums.Cargo;
import com.felipefreitas.ConectaClinica.enums.RoleFuncionario;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

public record FuncionarioRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100,message = "Nome não pode passar de 100 caracteres")
        String nome,

        @NotBlank(message = "O cpf é obrigatório")
        @CPF(message = "O formato de cpf é inválido")
        String cpf,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O formato de email é inválido")
        String email,

        @NotBlank(message = "A senha não pode estar em branco")
        @Size(min = 8)
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "A senha deve conter pelo menos uma letra maiúscula, um número e um caractere especial" )
        String senha,

        @NotNull(message = "O cargo é obrigatório")
        Cargo cargo,

        @NotNull(message = "A role do funcionário é obrigatório")
        RoleFuncionario role
) {
}
