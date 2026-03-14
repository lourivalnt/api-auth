package com.auth.infrastructure.observability;

import io.micrometer.core.instrument.*;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthMetrics {

    private final MeterRegistry registry;

    private Counter loginSuccess;
    private Counter loginFailure;

    @PostConstruct
    public void init() {
        loginSuccess = registry.counter("auth.login.success");
        loginFailure = registry.counter("auth.login.failure");
    }

    public void loginSuccess() {
        loginSuccess.increment();
    }

    public void loginFailure() {
        loginFailure.increment();
    }
}