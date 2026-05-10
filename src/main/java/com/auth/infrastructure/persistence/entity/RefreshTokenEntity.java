package com.auth.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "session_id", nullable = false)
    private UUID sessionId;

    @Column(nullable = false, unique = true, length = 1000)
    private String token;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    private boolean revoked;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}