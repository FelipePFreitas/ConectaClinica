package com.felipefreitas.ConectaClinica.controller;

import com.felipefreitas.ConectaClinica.dto.especialidade.EspecialidadeRequestDTO;
import com.felipefreitas.ConectaClinica.dto.especialidade.EspecialidadeResponseDTO;
import com.felipefreitas.ConectaClinica.service.EspecialidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/especialidades")
@RequiredArgsConstructor
@Tag(name = "Especialidades", description = "Endpoints para gerenciamento das especialidade (Acesso exclusivo para GERENTE)")
@SecurityRequirement(name = "bearer-key")
public class EspecialidadeController {

    private final EspecialidadeService especialidadeService;

    @PostMapping
    @Operation(summary = "Cadastrar especialidades", description = "Cadastra uma nova especialidade na clinica.")
    public ResponseEntity<EspecialidadeResponseDTO> cadastrarEspecialidade(@RequestBody @Valid EspecialidadeRequestDTO especialidadeRequestDTO) {
        EspecialidadeResponseDTO especialidadeResponseDTO = especialidadeService.cadastrarEspecialidade(especialidadeRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(especialidadeResponseDTO);
    }

    @GetMapping
    @Operation(summary = "Listar especialidades", description = "Listar todas as especialidades em ordem alfabética")
    public ResponseEntity<List<EspecialidadeResponseDTO>> listarTodasEspecialidades() {
        List<EspecialidadeResponseDTO> listaEspecialidades = especialidadeService.listarEspecialidade();
        return ResponseEntity.ok(listaEspecialidades);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar especialidades", description = "Deletar especialidade, desde que não esteja atrelado a nenhum " +
            "funcionário")
    public ResponseEntity<Void> deletarEspecialidde(@PathVariable Long id) {
        especialidadeService.deletarEspecialidadePorId(id);
        return ResponseEntity.noContent().build();
    }
}
