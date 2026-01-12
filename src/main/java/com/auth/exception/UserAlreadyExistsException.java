package com.auth.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super("Já existe um usuário cadastrado com o email: " + message);
    }
}
