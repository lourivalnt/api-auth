package com.auth.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Auth API",
        version = "1.0.0",
        description = """
        ## 🔐 API de Autenticação e Autorização JWT

        Esta API implementa autenticação baseada em **JWT (Access + Refresh Token)**.

        ### 🔁 Fluxo de autenticação

        1️⃣ **Login**
        - Endpoint: `POST /auth/login`
        - Retorna:
          - `accessToken` (JWT curto)
          - `refreshToken` (longo)

        2️⃣ **Acesso a endpoints protegidos**
        - Header:
          ```
          Authorization: Bearer <accessToken>
          ```

        3️⃣ **Refresh token**
        - Endpoint: `POST /auth/refresh`
        - Usado quando o access token expirar

        4️⃣ **Logout**
        - Endpoint: `POST /auth/logout`
        - Revoga o refresh token

        ### ⚠️ Erros
        Todos os erros seguem o padrão **RFC 7807 – Problem Details**.
        """,
        contact = @Contact(
            name = "Equipe Auth",
            email = "suporte@auth.com"
        ),
        license = @License(
            name = "MIT"
        )
    ),
    servers = {
        @Server(
            description = "Ambiente Local", url = "http://localhost:8080"),
        @Server(
            description = "Localhost (Docker)", url = "http://localhost:8081"),
        @Server(
            description = "Produção", url = "https://api.auth.com"),
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class OpenApiConfig {
}
