package com.auth.infrastructure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de login")
public record LoginRequest(

        @Schema(example = "user@email.com")
        String email,

        @Schema(example = "123456")
        String password
) {}
