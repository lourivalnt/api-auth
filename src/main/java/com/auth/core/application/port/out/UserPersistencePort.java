package com.auth.core.application.port.out;

import java.util.Optional;
import java.util.UUID;

import com.auth.core.domain.model.User;

public interface UserPersistencePort {

    User create(String email, String password);

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);
}

