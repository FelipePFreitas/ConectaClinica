package com.felipefreitas.ConectaClinica.controller;

import com.felipefreitas.ConectaClinica.dto.funcionario.FuncionarioRequestDTO;
import com.felipefreitas.ConectaClinica.dto.funcionario.FuncionarioResponseDTO;
import com.felipefreitas.ConectaClinica.entity.FuncionarioEntity;
import com.felipefreitas.ConectaClinica.service.FuncionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/funcionarios")
@RequiredArgsConstructor
@Tag(name = "Funcionários", description = "Endpoints para gerenciamento de funcionários (Acesso exclusivo para GERENTE)")
@SecurityRequirement(name = "bearer-key")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @PostMapping
    @Operation(summary = "Cadastrar funcionário", description = "Cadastra um novo funcionário Administrativo na " +
            "clínica.")
    public ResponseEntity<FuncionarioResponseDTO> cadastrarFuncionario(@RequestBody @Valid FuncionarioRequestDTO funcionarioRequestDTO) {
        FuncionarioResponseDTO funcionarioSalvo = funcionarioService.cadastrarFuncionarioAdministrativo(funcionarioRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioSalvo);
    }

    @GetMapping("/cpf/{cpf}")
    @Operation(summary = "Buscar funcionário por CPF", description = "Retorna os dados de um funcionário a partir do seu CPF.")
    public ResponseEntity<FuncionarioResponseDTO> buscarFuncionarioCpf(@PathVariable String cpf) {
        FuncionarioResponseDTO funcionarioBuscado = funcionarioService.buscarFuncionarioPorCpf(cpf);
        return ResponseEntity.ok(funcionarioBuscado);
    }

    @GetMapping
    @Operation(summary = "Listar funcionários paginados", description = "Retorna a lista de todos os funcionários com suporte a paginação e ordenação.")
    public ResponseEntity<Page<FuncionarioResponseDTO>> listarFuncionarios(@PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        Page<FuncionarioResponseDTO> pagina = funcionarioService.listarTodosFuncionarios(pageable);
        return ResponseEntity.ok(pagina);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados do funcionário", description = "Atualiza os dados cadastrais de um funcionário existente.")
    public ResponseEntity<FuncionarioResponseDTO> atualizarFuncionario(@PathVariable Long id,
                                                                @RequestBody FuncionarioRequestDTO funcionarioRequestDTO,
                                                                @AuthenticationPrincipal FuncionarioEntity funcionarioEntity) {
        FuncionarioResponseDTO funcionarioResponseDTO = funcionarioService.atualizarDadosFuncionario(id,
                funcionarioRequestDTO, funcionarioEntity);
        return ResponseEntity.ok(funcionarioResponseDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Inativar funcionário (Soft Delete)", description = "Altera o status do funcionário para inativo no sistema.")
    public ResponseEntity<Void> softDelete(
            @PathVariable Long id,
            @AuthenticationPrincipal FuncionarioEntity funcionarioEntity
    ) {
        funcionarioService.softDeleteFuncionario(id, funcionarioEntity);
        return ResponseEntity.noContent().build();
    }
}
