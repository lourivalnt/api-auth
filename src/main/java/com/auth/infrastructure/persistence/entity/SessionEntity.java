package com.auth.infrastructure.persistence.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sessions")
@Getter
@Setter
public class SessionEntity {

    @Id
    private UUID id;

    private UUID userId;

    private String device;
    private String ip;

    private boolean revoked;

    private Instant createdAt;
    private Instant lastSeenAt;
}

