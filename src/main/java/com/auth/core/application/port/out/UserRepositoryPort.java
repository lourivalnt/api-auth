package com.auth.core.application.port.out;

import com.auth.core.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);
}