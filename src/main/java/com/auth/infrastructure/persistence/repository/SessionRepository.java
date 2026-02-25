package com.auth.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.infrastructure.persistence.entity.SessionEntity;

public interface SessionRepository
        extends JpaRepository<SessionEntity, UUID> {

    List<SessionEntity> findByUserIdAndRevokedFalse(UUID userId);
}

