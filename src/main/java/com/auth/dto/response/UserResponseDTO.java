package com.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Dados do usuário autenticado")
public record UserResponseDTO(

    @Schema(example = "1")
    Long id,

    @Schema(example = "João Silva")
    String name,

    @Schema(example = "joao@email.com")
    String email,

    @Schema(example = "ROLE_USER")
    String role,

    @Schema(example = "true")
    boolean active
) {}
