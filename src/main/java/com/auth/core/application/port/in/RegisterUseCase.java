package com.auth.core.application.port.in;

import com.auth.web.dto.request.RegisterRequest;
import com.auth.web.dto.response.AuthResponse;

public interface RegisterUseCase {

    AuthResponse register(RegisterRequest request);
}