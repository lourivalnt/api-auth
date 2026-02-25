package com.auth.infrastructure.cache.repository;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SessionCacheRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String PREFIX = "session:";

    public void save(UUID sessionId, Object session) {
        redisTemplate.opsForValue()
                .set(PREFIX + sessionId, session, Duration.ofHours(24));
    }

    public Optional<Object> find(UUID sessionId) {
        return Optional.ofNullable(
                redisTemplate.opsForValue()
                        .get(PREFIX + sessionId)
        );
    }

    public void delete(UUID sessionId) {
        redisTemplate.delete(PREFIX + sessionId);
    }
}

