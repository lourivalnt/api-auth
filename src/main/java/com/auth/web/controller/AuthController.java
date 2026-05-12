package com.auth.web.controller;

import com.auth.core.application.port.in.LoginUseCase;
import com.auth.core.application.port.in.RefreshTokenUseCase;
import com.auth.core.application.port.in.RegisterUseCase;
import com.auth.web.dto.request.LoginRequest;
import com.auth.web.dto.request.RefreshTokenRequest;
import com.auth.web.dto.request.RegisterRequest;
import com.auth.web.dto.response.AuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication operations")
public class AuthController {

    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    @Operation(summary = "Register user", description = "Creates a new user account")
    @PostMapping("/register")
    public AuthResponse register(
            @Valid @RequestBody RegisterRequest request) {

        return registerUseCase.register(request);
    }

    @Operation(summary = "Authenticate user", description = "Authenticates user and returns JWT tokens")
    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request) {

        return loginUseCase.login(request);
    }

    @Operation(summary = "Refresh access token", description = "Generates new JWT using refresh token")
    @PostMapping("/refresh")
    public AuthResponse refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        return refreshTokenUseCase.refreshToken(request);
    }
}