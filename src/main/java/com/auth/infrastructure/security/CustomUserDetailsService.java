package com.auth.infrastructure.security;

import org.springframework.stereotype.Service;

import com.auth.infrastructure.persistence.repository.UserRepository;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import lombok.RequiredArgsConstructor;

import com.auth.infrastructure.persistence.entity.UserEntity;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return toUserDetails(user);
    }

    public UserDetails loadUserById(UUID userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return toUserDetails(user);
    }

    private UserDetails toUserDetails(UserEntity user) {

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getId().toString())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }
}
