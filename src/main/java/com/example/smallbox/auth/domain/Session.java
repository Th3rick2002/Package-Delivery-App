package com.example.smallbox.auth.domain;

import com.example.smallbox.shared.domain.UserId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Session {
    private final UUID id;
    private final String refreshTokenHash;
    private final UserId userId;
    private boolean revoked;
    private final LocalDateTime expiredAt;
    private final String ipAddress;
    private final String userAgent;
    private final LocalDateTime createdAt;
    private LocalDateTime lastUsedAt;
    private UUID replacedBy;

    public static Session create(
            String refreshTokenHash,
            UserId userId,
            LocalDateTime expiredAt,
            String ipAddress,
            String userAgent
    ) {
        return Session.builder()
                .id(UUID.randomUUID())
                .refreshTokenHash(refreshTokenHash)
                .userId(userId)
                .revoked(false)
                .expiredAt(expiredAt)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .createdAt(LocalDateTime.now())
                .lastUsedAt(LocalDateTime.now())
                .build();
    }

    public void revoke() {
        this.revoked = true;
    }

    public void replace(UUID newSessionId) {
        this.revoked = true;
        this.replacedBy = newSessionId;
    }

    public void use() {
        this.lastUsedAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredAt);
    }

    public boolean isValid() {
        return !revoked && !isExpired();
    }
}
