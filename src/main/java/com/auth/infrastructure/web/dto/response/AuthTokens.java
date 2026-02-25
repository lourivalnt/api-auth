package com.auth.infrastructure.web.dto.response;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tokens de autenticação")
public record AuthTokens(

        @Schema(description = "JWT Access Token")
        String accessToken,

        @Schema(description = "Refresh Token")
        UUID refreshToken
) {}
