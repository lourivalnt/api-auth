package com.auth.infrastructure.persistence.adapter;

import com.auth.core.application.port.out.UserPersistencePort;
import com.auth.core.domain.model.Role;
import com.auth.core.domain.model.User;
import com.auth.infrastructure.persistence.entity.UserEntity;
import com.auth.infrastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPersistencePort {

    private final UserRepository repository;

    @Override
     public User create(String email, String password) {
        UserEntity entity = toEntity(email, password);
        return toDomain(repository.save(entity));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id)
                .map(this::toDomain);
    }

    private UserEntity toEntity(String email, String password) {
        UserEntity entity = new UserEntity();
        // entity.setId(user.getId());
        // entity.setName(user.getName());
        entity.setEmail(email);
        entity.setPassword(password);
        // entity.setRole(user.getRole().name());
        return entity;
    }

    private User toDomain(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(Role.valueOf(entity.getRole()))
                .build();
    }

}

