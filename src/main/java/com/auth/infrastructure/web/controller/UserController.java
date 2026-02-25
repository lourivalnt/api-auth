package com.auth.infrastructure.web.controller;

import com.auth.core.application.port.in.UserUseCase;
import com.auth.infrastructure.web.dto.request.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @PostMapping
    public ResponseEntity<UUID> createUser(
            @RequestBody @Valid CreateUserRequest request) {

        UUID id = userUseCase.createUser(
                request.email(),
                request.password()
        );

        return ResponseEntity.ok(id);
    }
}

