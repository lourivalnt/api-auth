package com.auth.core.application.service.impl;

import com.auth.core.application.port.in.LogoutUseCase;
import com.auth.core.application.port.in.SessionUseCase;
import com.auth.infrastructure.observability.metrics.AuthMetricsService;
import com.auth.infrastructure.persistence.entity.RefreshTokenEntity;
import com.auth.infrastructure.persistence.entity.SessionEntity;
import com.auth.infrastructure.persistence.repository.RefreshTokenJpaRepository;
import com.auth.infrastructure.persistence.repository.SessionJpaRepository;
import com.auth.web.dto.response.SessionResponse;
import com.auth.web.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl
    implements SessionUseCase, LogoutUseCase {

    private final SessionJpaRepository sessionRepository;

    private final RefreshTokenJpaRepository refreshTokenRepository;

    private final AuthMetricsService authMetricsService;

    @Override
    public List<SessionResponse> listSessions() {

        UUID userId = SecurityUtils.getCurrentUserId();

        return sessionRepository
            .findByUserIdAndRevokedFalse(userId)
            .stream()
            .map(session ->
                SessionResponse.builder()
                    .id(session.getId())
                    .device(session.getDevice())
                    .ipAddress(session.getIpAddress())
                    .createdAt(session.getCreatedAt())
                    .lastSeenAt(session.getLastSeenAt())
                    .build()
            )
            .toList();
    }

    @Override
    public void logout(UUID sessionId) {

        UUID userId = SecurityUtils.getCurrentUserId();

        SessionEntity session =
            sessionRepository
                .findByIdAndUserId(sessionId, userId)
                .orElseThrow(() ->
                    new RuntimeException("Session not found")
                );

        session.setRevoked(true);

        sessionRepository.save(session);

        revokeRefreshTokens(sessionId);

        authMetricsService.incrementLogout();

        
    }

    @Override
    public void logoutAll() {

        UUID userId = SecurityUtils.getCurrentUserId();

        List<SessionEntity> sessions =
            sessionRepository
                .findByUserIdAndRevokedFalse(userId);

        sessions.forEach(session -> {

            session.setRevoked(true);

            sessionRepository.save(session);

            revokeRefreshTokens(session.getId());

            authMetricsService.incrementLogout();
        });
    }

    private void revokeRefreshTokens(UUID sessionId) {

        List<RefreshTokenEntity> tokens =
            refreshTokenRepository
                .findAllBySessionId(sessionId);

        tokens.forEach(token -> {

            token.setRevoked(true);

            refreshTokenRepository.save(token);
        });
    }
}