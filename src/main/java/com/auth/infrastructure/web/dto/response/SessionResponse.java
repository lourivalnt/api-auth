package com.auth.infrastructure.web.dto.response;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Sessão ativa")
public record SessionResponse(

        @Schema(description = "ID da sessão")
        UUID sessionId,

        @Schema(example = "Chrome / Windows")
        String device,

        @Schema(example = "177.xxx.xxx")
        String ip,

        Instant createdAt,
        Instant lastSeenAt
) {}

