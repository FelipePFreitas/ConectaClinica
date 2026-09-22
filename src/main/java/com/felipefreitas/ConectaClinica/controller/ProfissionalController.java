package com.felipefreitas.ConectaClinica.controller;

import com.felipefreitas.ConectaClinica.dto.agendamento.ProfissionalRequestDTO;
import com.felipefreitas.ConectaClinica.dto.agendamento.ProfissionalResponseDTO;
import com.felipefreitas.ConectaClinica.service.ProfissionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/profissionais")
@RequiredArgsConstructor
@Tag(name = "Profissionais", description = "Cadastro mínimo de profissionais para agendamento de consultas")
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    @PostMapping
    @Operation(summary = "Cadastrar profissional")
    public ResponseEntity<ProfissionalResponseDTO> cadastrar(@RequestBody @Valid ProfissionalRequestDTO request,
                                                             Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profissionalService.cadastrar(request, authentication));
    }

    @GetMapping
    @Operation(summary = "Listar profissionais")
    public ResponseEntity<List<ProfissionalResponseDTO>> listar(Authentication authentication) {
        return ResponseEntity.ok(profissionalService.listar(authentication));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar profissional por identificador")
    public ResponseEntity<ProfissionalResponseDTO> buscar(@PathVariable UUID id, Authentication authentication) {
        return ResponseEntity.ok(profissionalService.buscar(id, authentication));
    }
}
