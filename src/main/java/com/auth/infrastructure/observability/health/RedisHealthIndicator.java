package com.auth.infrastructure.observability.health;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisHealthIndicator
    implements HealthIndicator {

    private final StringRedisTemplate redisTemplate;

    @Override
    public Health health() {

        try {

            redisTemplate.hasKey("health_check");

            return Health.up()
                .withDetail("redis", "available")
                .build();

        } catch (Exception ex) {

            return Health.down()
                .withDetail("redis", "unavailable")
                .build();
        }
    }
}