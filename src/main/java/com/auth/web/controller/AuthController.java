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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    @PostMapping("/register")
    public AuthResponse register(
            @Valid @RequestBody RegisterRequest request) {

        return registerUseCase.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request) {

        return loginUseCase.login(request);
    }

    @PostMapping("/refresh")
    public AuthResponse refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        return refreshTokenUseCase.refreshToken(request);
    }
}