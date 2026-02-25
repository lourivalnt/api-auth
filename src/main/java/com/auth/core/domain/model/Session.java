package com.auth.core.domain.model;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Session {

    private UUID id;
    private UUID userId;
    private String device;
    private String ip;
    private boolean revoked;
    private Instant createdAt;
    private Instant lastSeenAt;
}

