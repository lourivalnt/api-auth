package com.auth.core.application.port.in;

import java.util.UUID;

public interface LogoutUseCase {

    void logout(UUID sessionId);

    void logoutAll();
}