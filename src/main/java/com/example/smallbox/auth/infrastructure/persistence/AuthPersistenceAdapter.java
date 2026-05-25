package com.example.smallbox.auth.infrastructure.persistence;

import com.example.smallbox.auth.domain.AuthRepository;
import com.example.smallbox.auth.domain.Session;
import com.example.smallbox.auth.infrastructure.persistence.entities.SessionJpaEntity;
import com.example.smallbox.shared.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthPersistenceAdapter implements AuthRepository {
    private final SpringDataSessionRepository sessionRepository;

    @Override
    public Session save(Session session) {
        SessionJpaEntity entity = SessionMapper.toJpaEntity(session);
        return SessionMapper.toDomain(sessionRepository.save(entity));
    }

    @Override
    public Optional<Session> findByTokenHash(String tokenHash) {
        return sessionRepository.findByRefreshTokenHash(tokenHash).map(SessionMapper::toDomain);
    }

    @Override
    public Optional<Session> findById(UUID id) {
        return sessionRepository.findById(id).map(SessionMapper::toDomain);
    }

    @Override
    @Transactional
    public void revokeAllByUserId(UserId userId) {
        sessionRepository.revokeAllByUserId(userId.uuid());
    }

    @Override
    @Transactional
    public void revokeByDevice(UserId userId, String ipAddress, String userAgent) {
        sessionRepository.revokeByDevice(userId.uuid(), ipAddress, userAgent);
    }
}
