package com.auth.infrastructure.web.controller;

import com.auth.core.application.port.in.AuthUseCase;
import com.auth.infrastructure.web.dto.request.LoginRequest;
import com.auth.infrastructure.web.dto.request.RefreshTokenRequest;
import com.auth.infrastructure.web.dto.response.AuthTokens;
import com.auth.infrastructure.web.dto.response.SessionResponse;
import com.auth.infrastructure.web.dto.response.TokenResponse;
import com.auth.infrastructure.web.mapper.SessionMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Operações de autenticação")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;
    private final SessionMapper sessionMapper;

    @Operation(
            summary = "Autenticar usuário",
            description = "Realiza login com email/senha e retorna Access Token + Refresh Token"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthTokens> login(
            @RequestBody @Valid LoginRequest request) {

        AuthTokens tokens = authUseCase.login(
                request.email(),
                request.password());

        return ResponseEntity.ok(tokens);
    }

    @Operation(
            summary = "Renovar Access Token",
            description = "Gera novo Access Token usando Refresh Token válido"
    )
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(
            @RequestBody RefreshTokenRequest request) {

        String newAccessToken = authUseCase.refresh(request.refreshToken());

        return ResponseEntity.ok(
                new TokenResponse(newAccessToken));
    }

    @Operation(
            summary = "Listar sessões ativas",
            description = "Retorna todos os dispositivos conectados"
    )
    @PostMapping("/logout-all")
    public ResponseEntity<Void> logoutAll(Authentication authentication) {

        UUID userId = UUID.fromString(authentication.getName());

        authUseCase.logoutAll(userId);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar sessões ativas",
            description = "Retorna todos os dispositivos conectados"
    )
    @GetMapping("/sessions")
    public List<SessionResponse> sessions(Authentication authentication) {

        UUID userId = UUID.fromString(authentication.getName());

        return authUseCase.listSessions(userId)
                .stream()
                .map(sessionMapper::toResponse)
                .toList();
    }

    @Operation(
            summary = "Logout de sessão",
            description = "Revoga uma sessão específica do usuário"
    )
    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<Void> logoutSession(
            @PathVariable UUID sessionId,
            Authentication authentication) {

        UUID userId = UUID.fromString(authentication.getName());

        authUseCase.logoutSession(userId, sessionId);

        return ResponseEntity.noContent().build();
    }

}
