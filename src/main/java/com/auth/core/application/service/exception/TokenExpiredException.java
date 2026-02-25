package com.auth.core.application.service.exception;

public class TokenExpiredException extends RuntimeException { 

    public TokenExpiredException(String message) {
        super(message);
    }
}