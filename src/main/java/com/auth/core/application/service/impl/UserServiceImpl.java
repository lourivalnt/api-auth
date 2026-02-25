package com.auth.core.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.auth.core.application.port.in.UserUseCase;
import com.auth.core.application.port.out.UserPersistencePort;
import com.auth.core.domain.model.User;
import com.auth.infrastructure.security.PasswordEncoderAdapter;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserUseCase {

    private final UserPersistencePort userRepository;
    private final PasswordEncoderAdapter encoder;

    @Override
    public UUID createUser(String email, String password) {

        String encodedPassword = encoder.encode(password);

        var user = userRepository.create(email, encodedPassword);

        return user.getId();
    }

    @Override
    public User getUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
