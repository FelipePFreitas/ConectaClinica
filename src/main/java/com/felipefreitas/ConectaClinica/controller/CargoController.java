package com.felipefreitas.ConectaClinica.controller;


import com.felipefreitas.ConectaClinica.dto.cargo.CargoRequestDTO;
import com.felipefreitas.ConectaClinica.dto.cargo.CargoResponseDTO;
import com.felipefreitas.ConectaClinica.service.CargoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cargos")
@RequiredArgsConstructor
@Tag(name = "Cargos", description = "Endpoints para gerenciamento de cargo (Acesso exclusivo para GERENTE)")
@SecurityRequirement(name = "bearer-key")
public class CargoController {

    private final CargoService cargoService;

    @PostMapping
    @Operation(summary = "Cadastrar cargo", description = "Cadastra um novo cargo na clinica.")
   public ResponseEntity<CargoResponseDTO> cadastrarCargo (@RequestBody @Valid CargoRequestDTO cargoRequestDTO){
        CargoResponseDTO cargoResponseDTO = cargoService.cadastrarCargo(cargoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(cargoResponseDTO);
    }
}
