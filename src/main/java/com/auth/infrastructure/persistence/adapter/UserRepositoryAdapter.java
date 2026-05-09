package com.auth.infrastructure.persistence.adapter;

import com.auth.core.application.port.out.UserRepositoryPort;
import com.auth.core.domain.model.User;
import com.auth.infrastructure.persistence.mapper.UserMapper;
import com.auth.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter
    implements UserRepositoryPort {

    private final UserJpaRepository repository;
    private final UserMapper mapper;

    @Override
    public User save(User user) {

        return mapper.toDomain(
            repository.save(
                mapper.toEntity(user)
            )
        );
    }

    @Override
    public Optional<User> findByEmail(String email) {

        return repository.findByEmail(email)
            .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {

        return repository.findById(id)
            .map(mapper::toDomain);
    }
}