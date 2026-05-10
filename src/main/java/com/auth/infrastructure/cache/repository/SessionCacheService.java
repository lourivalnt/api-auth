package com.auth.infrastructure.cache.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionCacheService {

    private final StringRedisTemplate redisTemplate;

    public void cacheSession(
        UUID sessionId,
        UUID userId
    ) {

        String key = "session:" + sessionId;

        redisTemplate.opsForValue()
            .set(
                key,
                userId.toString(),
                Duration.ofHours(1)
            );
    }

    public String getSession(UUID sessionId) {

        return redisTemplate.opsForValue()
            .get("session:" + sessionId);
    }

    public void removeSession(UUID sessionId) {

        redisTemplate.delete(
            "session:" + sessionId
        );
    }
}