package com.auth.core.application.port.in;

import java.util.List;
import java.util.UUID;

import com.auth.core.domain.model.Session;
import com.auth.infrastructure.web.dto.response.AuthTokens;

public interface AuthUseCase {
    AuthTokens login(String email, String password);

    String refresh(UUID refreshToken);

    void logoutAll(UUID userId);

    List<Session> listSessions(UUID userId);

    void logoutSession(UUID userId, UUID sessionId);

}
