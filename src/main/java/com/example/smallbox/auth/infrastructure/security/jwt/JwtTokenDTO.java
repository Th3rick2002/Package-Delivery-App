package com.example.smallbox.auth.infrastructure.security.jwt;

public record JwtTokenDTO(
        String accessToken,
        String refreshToken
) {}
