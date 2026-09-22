package com.felipefreitas.ConectaClinica.controller;

import com.felipefreitas.ConectaClinica.dto.agendamento.PacienteRequestDTO;
import com.felipefreitas.ConectaClinica.dto.agendamento.PacienteResponseDTO;
import com.felipefreitas.ConectaClinica.service.PacienteService;
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
@RequestMapping("/pacientes")
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "Cadastro mínimo de pacientes para agendamento de consultas")
public class PacienteController {

    private final PacienteService pacienteService;

    @PostMapping
    @Operation(summary = "Cadastrar paciente")
    public ResponseEntity<PacienteResponseDTO> cadastrar(@RequestBody @Valid PacienteRequestDTO request,
                                                         Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.cadastrar(request, authentication));
    }

    @GetMapping
    @Operation(summary = "Listar pacientes")
    public ResponseEntity<List<PacienteResponseDTO>> listar(Authentication authentication) {
        return ResponseEntity.ok(pacienteService.listar(authentication));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar paciente por identificador")
    public ResponseEntity<PacienteResponseDTO> buscar(@PathVariable UUID id, Authentication authentication) {
        return ResponseEntity.ok(pacienteService.buscar(id, authentication));
    }
}
