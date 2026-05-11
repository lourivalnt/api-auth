package com.auth.infrastructure.observability.metrics;

import com.auth.infrastructure.persistence.repository.SessionJpaRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionMetricsService {

    private final SessionJpaRepository sessionRepository;

    private final MeterRegistry meterRegistry;

    @PostConstruct
    public void registerMetrics() {

        Gauge.builder(
            "auth_active_sessions",
            this,
            SessionMetricsService::countActiveSessions
        )
        .description("Active user sessions")
        .register(meterRegistry);
    }

    public double countActiveSessions() {

        return sessionRepository.findAll()
            .stream()
            .filter(session -> !session.isRevoked())
            .count();
    }
}