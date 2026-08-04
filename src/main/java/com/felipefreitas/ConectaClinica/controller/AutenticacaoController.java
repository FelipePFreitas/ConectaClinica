package com.felipefreitas.ConectaClinica.controller;

import com.felipefreitas.ConectaClinica.dto.login.LoginRequestDTO;
import com.felipefreitas.ConectaClinica.dto.login.LoginResponseDTO;
import com.felipefreitas.ConectaClinica.service.AutenticacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Login", description = "Endpoints para login e geração de tokens")
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    @PostMapping("/login")
    @Operation(summary = "Realizar login", description = "Autentica o usuário via e-mail/senha e retorna um Token JWT Bearer.")
    ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponseDTO = autenticacaoService.autenticar(loginRequestDTO);
        return ResponseEntity.ok(loginResponseDTO);
    }

}
