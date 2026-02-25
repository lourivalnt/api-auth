package com.auth.infrastructure.web.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auth.core.application.service.exception.InvalidRefreshTokenException;

import java.net.URI;
import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    /* =========================
       AUTH / SECURITY
       ========================= */

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request) {

        return buildProblem(
                HttpStatus.UNAUTHORIZED,
                "Credenciais inválidas",
                "Usuário ou senha inválidos",
                request
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request) {

        return buildProblem(
                HttpStatus.FORBIDDEN,
                "Acesso negado",
                "Você não tem permissão para acessar este recurso",
                request
        );
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ProblemDetail handleInvalidRefreshToken(
            InvalidRefreshTokenException ex,
            HttpServletRequest request) {

        return buildProblem(
                HttpStatus.UNAUTHORIZED,
                "Refresh token inválido",
                ex.getMessage(),
                request
        );
    }

    /* =========================
       VALIDATION
       ========================= */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildProblem(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                details,
                request
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        return buildProblem(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                ex.getMessage(),
                request
        );
    }

    /* =========================
       GENERIC
       ========================= */

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        return buildProblem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno",
                "Ocorreu um erro inesperado",
                request
        );
    }

    /* =========================
       BUILDER
       ========================= */

    private ProblemDetail buildProblem(
            HttpStatus status,
            String title,
            String detail,
            HttpServletRequest request) {

        ProblemDetail problem = ProblemDetail.forStatus(status);

        problem.setTitle(title);
        problem.setDetail(detail);
        problem.setType(URI.create("https://api.seusistema.com/errors/" + status.value()));
        problem.setInstance(URI.create(request.getRequestURI()));

        // 🔥 Campos extras (RFC permite extensões)
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("status", status.value());
        problem.setProperty("error", status.getReasonPhrase());

        return problem;
    }
}
