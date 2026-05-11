package com.auth.infrastructure.observability.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class AuthMetricsService {

    private final Counter loginSuccessCounter;

    private final Counter loginFailureCounter;

    private final Counter refreshTokenCounter;

    private final Counter logoutCounter;

    public AuthMetricsService(
        MeterRegistry meterRegistry
    ) {

        this.loginSuccessCounter =
            Counter.builder("auth_login_success_total")
                .description("Successful logins")
                .register(meterRegistry);

        this.loginFailureCounter =
            Counter.builder("auth_login_failure_total")
                .description("Failed logins")
                .register(meterRegistry);

        this.refreshTokenCounter =
            Counter.builder("auth_refresh_total")
                .description("Refresh token usage")
                .register(meterRegistry);

        this.logoutCounter =
            Counter.builder("auth_logout_total")
                .description("Logout operations")
                .register(meterRegistry);
    }

    public void incrementLoginSuccess() {
        loginSuccessCounter.increment();
    }

    public void incrementLoginFailure() {
        loginFailureCounter.increment();
    }

    public void incrementRefreshToken() {
        refreshTokenCounter.increment();
    }

    public void incrementLogout() {
        logoutCounter.increment();
    }
}