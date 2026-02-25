package com.auth.core.application.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.auth.core.domain.model.Session;

public interface SessionPersistencePort {

    Session save(Session session);

    Optional<Session> findById(UUID id);

    List<Session> findActiveByUser(UUID userId);

    void revoke(UUID sessionId);

    void revokeAllByUser(UUID userId);
}

