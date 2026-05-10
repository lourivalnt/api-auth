package com.auth.infrastructure.persistence.repository;

import com.auth.infrastructure.persistence.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    Optional<RefreshTokenEntity> findByToken(String token);

    Optional<RefreshTokenEntity> findBySessionId(UUID sessionId);

    List<RefreshTokenEntity> findAllBySessionId(UUID sessionId);
}