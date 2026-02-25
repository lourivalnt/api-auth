package com.auth.infrastructure.security.ratelimit;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final int MAX_ATTEMPTS = 5;
    private static final Duration WINDOW = Duration.ofMinutes(1);

    public boolean isBlocked(String ip) {

        String key = "login:attempts:" + ip;

        Object attemptsObj = redisTemplate.opsForValue().get(key);

        if (attemptsObj == null) {
            return false;
        }

        int attempts = (int) attemptsObj;

        return attempts >= MAX_ATTEMPTS;
    }

    public void registerAttempt(String ip) {

        String key = "login:attempts:" + ip;

        Object attemptsObj = redisTemplate.opsForValue().get(key);

        if (attemptsObj == null) {

            redisTemplate.opsForValue().set(key, 1, WINDOW);
            return;
        }

        int attempts = (int) attemptsObj;

        redisTemplate.opsForValue().increment(key);
    }

    public void reset(String ip) {
        redisTemplate.delete("login:attempts:" + ip);
    }
}