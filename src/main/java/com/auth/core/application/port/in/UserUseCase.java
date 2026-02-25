package com.auth.core.application.port.in;

import java.util.UUID;

import com.auth.core.domain.model.User;

public interface UserUseCase {

    UUID createUser(String email, String password);

    User getUser(UUID id);
}
