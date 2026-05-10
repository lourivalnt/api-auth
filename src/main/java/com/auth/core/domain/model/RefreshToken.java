package com.auth.core.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    private UUID id;

    private UUID sessionId;

    private String token;

    private LocalDateTime expiresAt;

    private boolean revoked;

    private LocalDateTime createdAt;
}