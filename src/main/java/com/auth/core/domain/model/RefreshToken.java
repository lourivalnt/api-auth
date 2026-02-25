package com.auth.core.domain.model;

import java.time.Instant;
import java.util.UUID;

import lombok.*;

@Getter
@Builder
public class RefreshToken {

    private UUID token;
    private UUID userId;
    private UUID sessionId;
    private Instant expiryDate;
}

