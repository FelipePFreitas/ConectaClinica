package com.felipefreitas.ConectaClinica.dto.login;

public record LoginResponseDTO(
        String token,
        String tipo, // Ex: "Bearer"
        Long expiracaoEm // Ex: tempo restante ou timestamp
) {}