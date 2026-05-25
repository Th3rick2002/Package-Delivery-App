package com.example.smallbox.auth.domain;

import com.example.smallbox.shared.domain.UserId;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository {
    Session save(Session session);
    Optional<Session> findByTokenHash(String tokenHash);
    Optional<Session> findById(UUID id);
    void revokeAllByUserId(UserId userId);
    void revokeByDevice(UserId userId, String ipAddress, String userAgent);
}
