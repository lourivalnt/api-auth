package com.auth.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.auth.core.application.port.out.SessionPersistencePort;
import com.auth.core.domain.model.Session;
import com.auth.infrastructure.cache.repository.SessionCacheRepository;
import com.auth.infrastructure.persistence.mapper.SessionEntityMapper;
import com.auth.infrastructure.persistence.repository.SessionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SessionPersistenceAdapter implements SessionPersistencePort {

    private final SessionRepository repository;
    private final SessionCacheRepository cache;
    private final SessionEntityMapper mapper;

    @Override
    public Session save(Session session) {

        var entity = mapper.toEntity(session);
        repository.save(entity);

        cache.save(session.getId(), session);

        return session;
    }

    @Override
    public Optional<Session> findById(UUID id) {

        var cached = cache.find(id);
        if (cached.isPresent()) {
            return Optional.of((Session) cached.get());
        }

        return repository.findById(id)
                .map(mapper::toDomain)
                .map(session -> {
                    cache.save(id, session);
                    return session;
                });
    }

    @Override
    public void revoke(UUID sessionId) {
        repository.deleteById(sessionId);
        cache.delete(sessionId);
    }

    @Override
    public List<Session> findActiveByUser(UUID userId) {
        return repository.findByUserIdAndRevokedFalse(userId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void revokeAllByUser(UUID userId) {

        var sessions = repository.findByUserIdAndRevokedFalse(userId);

        sessions.forEach(entity -> {
            entity.setRevoked(true);
            repository.save(entity);
            cache.delete(entity.getId());
        });
    }

}
