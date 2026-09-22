package com.felipefreitas.ConectaClinica.controller;

import com.felipefreitas.ConectaClinica.dto.agendamento.ConsultaCancelamentoRequestDTO;
import com.felipefreitas.ConectaClinica.dto.agendamento.ConsultaCriacaoRequestDTO;
import com.felipefreitas.ConectaClinica.dto.agendamento.ConsultaReagendamentoRequestDTO;
import com.felipefreitas.ConectaClinica.dto.agendamento.ConsultaResponseDTO;
import com.felipefreitas.ConectaClinica.dto.agendamento.ConsultaStatusRequestDTO;
import com.felipefreitas.ConectaClinica.service.ConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
@Tag(name = "Consultas", description = "Agendamento, cancelamento, reagendamento e status de consultas")
public class ConsultaController {

    private final ConsultaService consultaService;

    @PostMapping
    @Operation(summary = "Agendar consulta")
    public ResponseEntity<ConsultaResponseDTO> agendar(@RequestBody @Valid ConsultaCriacaoRequestDTO request,
                                                       Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.agendar(request, authentication));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar consulta por identificador")
    public ResponseEntity<ConsultaResponseDTO> buscar(@PathVariable UUID id, Authentication authentication) {
        return ResponseEntity.ok(consultaService.buscar(id, authentication));
    }

    @GetMapping(params = "pacienteId")
    @Operation(summary = "Listar consultas de um paciente")
    public ResponseEntity<List<ConsultaResponseDTO>> listarPorPaciente(@RequestParam UUID pacienteId,
                                                                       Authentication authentication) {
        return ResponseEntity.ok(consultaService.listarPorPaciente(pacienteId, authentication));
    }

    @GetMapping(params = "profissionalId")
    @Operation(summary = "Listar consultas de um profissional")
    public ResponseEntity<List<ConsultaResponseDTO>> listarPorProfissional(@RequestParam UUID profissionalId,
                                                                           Authentication authentication) {
        return ResponseEntity.ok(consultaService.listarPorProfissional(profissionalId, authentication));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Alterar status da consulta")
    public ResponseEntity<ConsultaResponseDTO> alterarStatus(@PathVariable UUID id,
                                                             @RequestBody @Valid ConsultaStatusRequestDTO request,
                                                             Authentication authentication) {
        return ResponseEntity.ok(consultaService.alterarStatus(id, request, authentication));
    }

    @PatchMapping("/{id}/cancelamento")
    @Operation(summary = "Cancelar consulta")
    public ResponseEntity<ConsultaResponseDTO> cancelar(@PathVariable UUID id,
                                                        @RequestBody @Valid ConsultaCancelamentoRequestDTO request,
                                                        Authentication authentication) {
        return ResponseEntity.ok(consultaService.cancelar(id, request, authentication));
    }

    @PatchMapping("/{id}/reagendamento")
    @Operation(summary = "Reagendar consulta")
    public ResponseEntity<ConsultaResponseDTO> reagendar(@PathVariable UUID id,
                                                         @RequestBody @Valid ConsultaReagendamentoRequestDTO request,
                                                         Authentication authentication) {
        return ResponseEntity.ok(consultaService.reagendar(id, request, authentication));
    }
}
