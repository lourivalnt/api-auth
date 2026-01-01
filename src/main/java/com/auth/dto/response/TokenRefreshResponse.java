package com.auth.dto.response;

public record TokenRefreshResponse(
        String accessToken,
        String tokenType
) {}
