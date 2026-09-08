package com.felipefreitas.ConectaClinica.controller;

import com.felipefreitas.ConectaClinica.dto.funcionario.FuncionarioRequestDTO;
import com.felipefreitas.ConectaClinica.dto.funcionario.FuncionarioResponseDTO;
import com.felipefreitas.ConectaClinica.service.FuncionarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<FuncionarioResponseDTO> cadastrarFuncionario(@RequestBody @Valid FuncionarioRequestDTO funcionarioRequestDTO){
        FuncionarioResponseDTO funcionarioResponseDTO = funcionarioService.cadastrarFuncionario(funcionarioRequestDTO);
        return ResponseEntity.ok().body(funcionarioResponseDTO);
    }

}
