package com.auth.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    private String device;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_seen_at")
    private LocalDateTime lastSeenAt;

    private boolean revoked;
}