package com.felipefreitas.ConectaClinica.service;


import com.felipefreitas.ConectaClinica.dto.auth.AuthTokenResponseDTO;
import com.felipefreitas.ConectaClinica.dto.auth.LoginRequestDTO;
import com.felipefreitas.ConectaClinica.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthTokenResponseDTO authenticate(LoginRequestDTO request) {
        log.info("Iniciando autenticação para login={}", request.login());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.login(), request.senha())
        );

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(principal);
        log.info("Autenticação realizada com sucesso para login={}", principal.getUsername());

        return new AuthTokenResponseDTO("Bearer", token, jwtService.getExpirationMillis());
    }
}
