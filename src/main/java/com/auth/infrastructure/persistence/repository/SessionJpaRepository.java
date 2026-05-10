package com.auth.infrastructure.persistence.repository;

import com.auth.infrastructure.persistence.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionJpaRepository
        extends JpaRepository<SessionEntity, UUID> {

    List<SessionEntity> findByUserIdAndRevokedFalse(UUID userId);

    Optional<SessionEntity> findByIdAndUserId(UUID id, UUID userId);
}