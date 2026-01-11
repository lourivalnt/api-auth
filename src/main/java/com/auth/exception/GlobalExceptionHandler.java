package com.auth.exception;

import java.time.Instant;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice()
public class GlobalExceptionHandler {

    private ProblemDetails buildProblem(
            HttpStatus status,
            String title,
            String detail,
            String type,
            HttpServletRequest request
    ) {
        return ProblemDetails.builder()
                .type(type)
                .title(title)
                .status(status.value())
                .detail(detail)
                .instance(request.getRequestURI())
                .timestamp(Instant.now())
                .build();
    }

    // 🔐 Credenciais inválidas
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetails> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(buildProblem(
                        HttpStatus.UNAUTHORIZED,
                        "Credenciais inválidas",
                        ex.getMessage(),
                        "https://api.auth.com/errors/invalid-credentials",
                        request
                ));
    }

    // 🚫 Usuário desativado
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ProblemDetails> handleDisabledUser(
            DisabledException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(buildProblem(
                        HttpStatus.FORBIDDEN,
                        "Usuário desativado",
                        ex.getMessage(),
                        "https://api.auth.com/errors/user-disabled",
                        request
                ));
    }

    // 🔒 Acesso negado
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetails> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(buildProblem(
                        HttpStatus.FORBIDDEN,
                        "Acesso negado",
                        ex.getMessage(),
                        "https://api.auth.com/errors/access-denied",
                        request
                ));
    }

    // 🔄 Refresh token inválido / expirado
    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ProblemDetails> handleRefreshToken(
            RefreshTokenException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(buildProblem(
                        HttpStatus.UNAUTHORIZED,
                        "Refresh token inválido",
                        ex.getMessage(),
                        "https://api.auth.com/errors/refresh-token",
                        request
                ));
    }

    // 🔑 Erros de autenticação (JWT, Security)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ProblemDetails> handleAuthentication(
            AuthenticationException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(buildProblem(
                        HttpStatus.UNAUTHORIZED,
                        "Erro de autenticação",
                        ex.getMessage(),
                        "https://api.auth.com/errors/authentication",
                        request
                ));
    }

    // 💥 Erro inesperado (fallback ÚNICO)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetails> handleGeneric(
            Exception ex,
            HttpServletRequest request
    ) {

        

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildProblem(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Erro interno",
                        "Ocorreu um erro inesperado",
                        "https://api.auth.com/errors/internal",
                        request
                ));
    }
}