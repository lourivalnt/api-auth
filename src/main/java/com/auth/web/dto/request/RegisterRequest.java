package com.auth.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @Schema(example = "user@email.com", description = "User email")
    @Email
    @NotBlank
    private String email;

    @Schema(example = "StrongPassword123", description = "User password")
    @NotBlank
    private String password;
}