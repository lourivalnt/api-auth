package com.auth.web.controller;

import com.auth.core.application.port.in.LogoutUseCase;
import com.auth.core.application.port.in.SessionUseCase;
import com.auth.web.dto.response.SessionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionUseCase sessionUseCase;

    private final LogoutUseCase logoutUseCase;

    @GetMapping
    public List<SessionResponse> listSessions() {

        return sessionUseCase.listSessions();
    }

    @PostMapping("/{sessionId}/logout")
    public void logout(
        @PathVariable UUID sessionId
    ) {

        logoutUseCase.logout(sessionId);
    }

    @PostMapping("/logout-all")
    public void logoutAll() {

        logoutUseCase.logoutAll();
    }
}