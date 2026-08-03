package com.felipefreitas.ConectaClinica.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearer-key";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // 1. Informações Globais da Aplicação
                .info(new Info()
                        .title("ConectaClínica API")
                        .description("API RESTful para gestão de consultas, exames, pacientes e corpo médico com autenticação JWT e controle de acesso por cargos.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Felipe Freitas")
                                .email("felipefreitas21081991@gmail.com")
                                .url("https://github.com/FelipePFreitas"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))

                // 2. Aplica a exigência de segurança JWT globalmente no Swagger
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))

                // 3. Define o esquema do Bearer Token no Swagger
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Insira o token JWT no formato: Bearer {token} para autorizar as requisições.")));
    }
}