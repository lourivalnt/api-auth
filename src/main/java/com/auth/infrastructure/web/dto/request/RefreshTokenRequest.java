package com.auth.infrastructure.web.dto.request;

import java.util.UUID;

public record RefreshTokenRequest(UUID refreshToken) {
}
