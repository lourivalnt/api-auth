package com.auth.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.entity.User;
import com.auth.repository.UserRepository;
import com.auth.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder
            .getContext()
            .getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
            .orElseThrow(() -> 
                new IllegalStateException("Usuário autenticado não encontrado"));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> 
                new IllegalArgumentException("Usuário não encontrado"));
    }

}
