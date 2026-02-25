package com.auth.core.application.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.auth.core.application.port.out.SessionPersistencePort;
import com.auth.core.domain.model.Session;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionPersistencePort sessionPersistencePort;

    public Session create(UUID userId, String device, String ip) {

        Session session = new Session(
                UUID.randomUUID(),
                userId,
                device,
                ip,
                false,
                Instant.now(),
                Instant.now());

        return sessionPersistencePort.save(session);
    }

    public void revokeAll(UUID userId) {
        sessionPersistencePort.revokeAllByUser(userId);
    }

    public List<Session> findActiveByUser(UUID userId) {
        return sessionPersistencePort.findActiveByUser(userId);
    }

    public void revoke(UUID sessionId) {
        sessionPersistencePort.revoke(sessionId);
    }

}
