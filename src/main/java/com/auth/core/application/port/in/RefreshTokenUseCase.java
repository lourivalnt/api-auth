package com.auth.core.application.port.in;

import com.auth.web.dto.request.RefreshTokenRequest;
import com.auth.web.dto.response.AuthResponse;

public interface RefreshTokenUseCase {

    AuthResponse refreshToken(
        RefreshTokenRequest request
    );
}