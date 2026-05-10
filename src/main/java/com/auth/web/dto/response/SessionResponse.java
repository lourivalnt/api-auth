package com.auth.web.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class SessionResponse {

    private UUID id;

    private String device;

    private String ipAddress;

    private LocalDateTime createdAt;

    private LocalDateTime lastSeenAt;
}