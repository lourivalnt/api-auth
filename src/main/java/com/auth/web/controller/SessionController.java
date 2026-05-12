package com.auth.web.controller;

import com.auth.core.application.port.in.LogoutUseCase;
import com.auth.core.application.port.in.SessionUseCase;
import com.auth.web.dto.response.SessionResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
@Tag(name = "Sessions", description = "Session management operations")
public class SessionController {

    private final SessionUseCase sessionUseCase;

    private final LogoutUseCase logoutUseCase;

    @Operation(summary = "List active sessions", description = "Returns all active user sessions")
    @GetMapping
    public List<SessionResponse> listSessions() {

        return sessionUseCase.listSessions();
    }

    @Operation(summary = "Logout device", description = "Revokes a specific session")
    @PostMapping("/{sessionId}/logout")
    public void logout(
            @PathVariable UUID sessionId) {

        logoutUseCase.logout(sessionId);
    }

    @Operation(summary = "Logout all devices", description = "Revokes all active sessions")
    @PostMapping("/logout-all")
    public void logoutAll() {

        logoutUseCase.logoutAll();
    }
}