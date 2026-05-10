package com.auth.infrastructure.security.ratelimit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RateLimitService {

    private static final int MAX_ATTEMPTS = 5;

    private static final Duration WINDOW =
        Duration.ofMinutes(1);

    private final StringRedisTemplate redisTemplate;

    public boolean isAllowed(String ip) {

        String key = "rate_limit:" + ip;

        String current =
            redisTemplate.opsForValue().get(key);

        int attempts =
            current == null ? 0 : Integer.parseInt(current);

        if (attempts >= MAX_ATTEMPTS) {
            return false;
        }

        Long increment =
            redisTemplate.opsForValue().increment(key);

        if (increment != null && increment == 1) {

            redisTemplate.expire(key, WINDOW);
        }

        return true;
    }
}