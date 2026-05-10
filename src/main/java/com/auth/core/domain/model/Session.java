package com.auth.core.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    private UUID id;

    private UUID userId;

    private String device;

    private String ipAddress;

    private LocalDateTime createdAt;

    private LocalDateTime lastSeenAt;

    private boolean revoked;
}