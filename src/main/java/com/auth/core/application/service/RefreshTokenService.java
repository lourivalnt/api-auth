package com.auth.core.application.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth.core.domain.model.RefreshToken;
import com.auth.core.application.exception.RefreshTokenException;
import com.auth.core.application.port.out.RefreshTokenPersistencePort;
import com.auth.core.application.port.out.SessionPersistencePort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenPersistencePort repository;
    private final SessionPersistencePort sessionPersistencePort;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    /*
        ✅ CREATE TOKEN
     */
    public RefreshToken create(UUID userId, UUID sessionId) {

        RefreshToken token = RefreshToken.builder()
                .token(UUID.randomUUID())
                .userId(userId)
                .sessionId(sessionId)
                .expiryDate(
                        Instant.now().plusMillis(refreshExpiration)
                )
                .build();

        return repository.save(token);
    }

    /*
        ✅ VALIDATE TOKEN
     */
    public RefreshToken validate(UUID tokenValue) {

        RefreshToken stored = repository.findByToken(tokenValue)
                .orElseThrow(() -> new RefreshTokenException("Invalid refresh token"));

        if (stored.getExpiryDate().isBefore(Instant.now())) {

            repository.revoke(tokenValue);

            throw new RuntimeException("Refresh token expired");
        }

        return stored;
    }

    /*
        🔥 ROTATION + REUSE DETECTION
     */
    public RefreshToken rotateWithReuseDetection(UUID tokenValue) {

        RefreshToken stored = repository.findByToken(tokenValue)
                .orElseThrow(() -> new RefreshTokenException("Invalid refresh token"));

        if (repository.findByToken(tokenValue).isEmpty()) {

            handleRefreshTokenReuse(stored);

            throw new RuntimeException("Refresh token reuse detected");
        }

        if (stored.getExpiryDate().isBefore(Instant.now())) {

            repository.revoke(tokenValue);

            throw new RuntimeException("Refresh token expired");
        }


        repository.revoke(tokenValue);

        return create(stored.getUserId(), stored.getSessionId());
    }

    /*
        🚨 REUSE DETECTED
     */
    private void handleRefreshTokenReuse(RefreshToken token) {

        UUID userId = token.getUserId();

        // derruba TODAS sessões
        sessionPersistencePort.revokeAllByUser(userId);

        // revoga TODOS refresh tokens
        repository.revokeAllByUser(userId);
    }
}
