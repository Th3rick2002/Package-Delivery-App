package com.example.smallbox.auth.infrastructure.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenBlacklistVerifier {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String BLACKLIST_PREFIX = "jwt:blacklist:";

    public boolean isTokenRevoked(String token) {
        if (token == null || token.isBlank()) {
            return true;
        }
        // Comprueba si el string exacto de este accessToken está en la lista negra
        return redisTemplate.hasKey(BLACKLIST_PREFIX + token);
    }
}
