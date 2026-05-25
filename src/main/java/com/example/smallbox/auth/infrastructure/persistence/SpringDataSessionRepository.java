package com.example.smallbox.auth.infrastructure.persistence;

import com.example.smallbox.auth.infrastructure.persistence.entities.SessionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataSessionRepository extends JpaRepository<SessionJpaEntity, UUID> {
    Optional<SessionJpaEntity> findByRefreshTokenHash(String refreshTokenHash);

    @Modifying
    @Query("UPDATE SessionJpaEntity s SET s.revoked = true WHERE s.userId = :userId")
    void revokeAllByUserId(UUID userId);

    @Modifying
    @Query("UPDATE SessionJpaEntity s SET s.revoked = true WHERE s.userId = :userId AND s.ipAddress = :ip AND s.userAgent = :ua")
    void revokeByDevice(UUID userId, String ip, String ua);
}
