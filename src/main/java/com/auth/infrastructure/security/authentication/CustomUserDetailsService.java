package com.auth.infrastructure.security.authentication;

import com.auth.infrastructure.persistence.entity.UserEntity;
import com.auth.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
    implements UserDetailsService {

    private final UserJpaRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email)
        throws UsernameNotFoundException {

        UserEntity user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                new UsernameNotFoundException("User not found")
            );

        return buildUserDetails(user);
    }

    public CustomUserDetails loadUserById(UUID userId) {

        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() ->
                new UsernameNotFoundException("User not found")
            );

        return buildUserDetails(user);
    }

    private CustomUserDetails buildUserDetails(UserEntity user) {

        return new CustomUserDetails(
            user.getId().toString(),
            user.getEmail(),
            user.getPassword(),
            List.of(
                new SimpleGrantedAuthority(
                    "ROLE_" + user.getRole()
                )
            )
        );
    }
}