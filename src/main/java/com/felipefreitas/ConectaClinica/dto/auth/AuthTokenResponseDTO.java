package com.felipefreitas.ConectaClinica.dto.auth;

public record AuthTokenResponseDTO(
        String tokenType,
        String accessToken,
        long expiresInMillis
) {
}
