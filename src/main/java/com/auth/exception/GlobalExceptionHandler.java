package com.auth.exception;

import java.net.URI;
import java.time.OffsetDateTime;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ProblemDetail build(
            HttpStatus status,
            String title,
            String detail,
            String type,
            HttpServletRequest request
    ) {
        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);
        problem.setType(URI.create(type));
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("timestamp", OffsetDateTime.now());
        return problem;
    }

    /* ============================
       401 - Credenciais inválidas
       ============================ */
    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request) {

        return build(
            HttpStatus.UNAUTHORIZED,
            "Unauthorized",
            "Credenciais inválidas",
            "https://api.auth.com/errors/unauthorized",
            request
        );
    }

    /* ============================
       403 - Acesso negado
       ============================ */
    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(
            HttpServletRequest request) {

        return build(
            HttpStatus.FORBIDDEN,
            "Forbidden",
            "Você não tem permissão para acessar este recurso",
            "https://api.auth.com/errors/forbidden",
            request
        );
    }

    /* ============================
       400 - Validação Bean Validation
       ============================ */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        ProblemDetail problem = build(
            HttpStatus.BAD_REQUEST,
            "Validation error",
            "Erro de validação nos campos da requisição",
            "https://api.auth.com/errors/validation",
            request
        );

        problem.setProperty(
            "errors",
            ex.getBindingResult()
              .getFieldErrors()
              .stream()
              .map(e -> e.getField() + ": " + e.getDefaultMessage())
              .toList()
        );

        return problem;
    }

    /* ============================
       404 - Usuário não encontrado (DOMÍNIO)
       ============================ */
    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFound(
            UserNotFoundException ex,
            HttpServletRequest request) {

        return build(
            HttpStatus.NOT_FOUND,
            "Usuário não encontrado",
            ex.getMessage(),
            "https://api.auth.com/errors/user-not-found",
            request
        );
    }

    /* ============================
       400 - Requisição inválida
       ============================ */
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        return build(
            HttpStatus.BAD_REQUEST,
            "Requisição inválida",
            ex.getMessage(),
            "https://api.auth.com/errors/bad-request",
            request
        );
    }

    /* ============================
       500 - Erro inesperado
       ============================ */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        return build(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Internal Server Error",
            "Ocorreu um erro inesperado",
            "https://api.auth.com/errors/internal",
            request
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail handleUserAlreadyExists(
                UserAlreadyExistsException ex,
                HttpServletRequest request) {

        return build(
                HttpStatus.CONFLICT,
                "Usuário já existe",
                ex.getMessage(),
                "https://api.auth.com/errors/user-already-exists",
                request
        );
    }

}

