package com.auth.infrastructure.persistence.adapter;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.auth.core.application.port.out.RefreshTokenPersistencePort;
import com.auth.core.domain.model.RefreshToken;

import com.auth.infrastructure.persistence.repository.RefreshTokenRepository;
import com.auth.infrastructure.persistence.mapper.RefreshTokenMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RefreshTokenPersistenceAdapter
        implements RefreshTokenPersistencePort {

    private final RefreshTokenRepository repository;
    private final RefreshTokenMapper mapper;

    @Override
    public RefreshToken save(RefreshToken token) {

        var entity = mapper.toEntity(token);
        repository.save(entity);

        return token;
    }

    @Override
    public Optional<RefreshToken> findByToken(UUID tokenValue) {

        return repository.findById(tokenValue)
                .map(mapper::toDomain);
    }

    @Override
    public void revoke(UUID tokenValue) {

        repository.findById(tokenValue)
                .ifPresent(entity -> {
                    repository.delete(entity);
                });
    }

    @Override
    public void revokeAllByUser(UUID userId) {

        repository.findByUserId(userId)
                .forEach(repository::delete);
    }
}
