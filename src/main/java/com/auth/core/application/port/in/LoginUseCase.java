package com.auth.core.application.port.in;

import com.auth.web.dto.request.LoginRequest;
import com.auth.web.dto.response.AuthResponse;

public interface LoginUseCase {

    AuthResponse login(LoginRequest request);
}