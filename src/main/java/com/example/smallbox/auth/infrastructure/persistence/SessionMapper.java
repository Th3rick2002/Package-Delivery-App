package com.example.smallbox.auth.infrastructure.persistence;

import com.example.smallbox.auth.domain.Session;
import com.example.smallbox.auth.infrastructure.persistence.entities.SessionJpaEntity;
import com.example.smallbox.shared.domain.UserId;

public class SessionMapper {
    public static Session toDomain(SessionJpaEntity entity) {
        if (entity == null) return null;
        return Session.builder()
                .id(entity.getId())
                .refreshTokenHash(entity.getRefreshTokenHash())
                .userId(new UserId(entity.getUserId()))
                .revoked(entity.isRevoked())
                .expiredAt(entity.getExpiredAt())
                .ipAddress(entity.getIpAddress())
                .userAgent(entity.getUserAgent())
                .createdAt(entity.getCreatedAt())
                .lastUsedAt(entity.getLastUsedAt())
                .replacedBy(entity.getReplacedBy())
                .build();
    }

    public static SessionJpaEntity toJpaEntity(Session domain) {
        if (domain == null) return null;
        SessionJpaEntity entity = new SessionJpaEntity();
        entity.setId(domain.getId());
        entity.setRefreshTokenHash(domain.getRefreshTokenHash());
        entity.setUserId(domain.getUserId().uuid());
        entity.setRevoked(domain.isRevoked());
        entity.setExpiredAt(domain.getExpiredAt());
        entity.setIpAddress(domain.getIpAddress());
        entity.setUserAgent(domain.getUserAgent());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setLastUsedAt(domain.getLastUsedAt());
        entity.setReplacedBy(domain.getReplacedBy());
        return entity;
    }
}
