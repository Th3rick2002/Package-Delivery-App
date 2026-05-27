package com.example.smallbox.auth.application;

import com.example.smallbox.auth.domain.AuthRepository;
import com.example.smallbox.auth.domain.Session;
import com.example.smallbox.auth.infrastructure.security.jwt.JWTType;
import com.example.smallbox.auth.infrastructure.security.jwt.JwtService;
import com.example.smallbox.auth.infrastructure.security.jwt.JwtTokenDTO;
import com.example.smallbox.auth.domain.exception.InvalidTokenException;
import com.example.smallbox.auth.domain.exception.SessionNotFoundException;
import com.example.smallbox.auth.domain.exception.TokenRevokedException;
import com.example.smallbox.shared.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final AuthRepository authRepository;
    private final JwtService jwtService;

    @Value("${jwt.expiration-time-refresh-ms}")
    private long refreshTokenExpirationMs;

    @Transactional
    public void saveSession(UUID userId, String token, String ip, String ua) {
        LocalDateTime expiredAt = LocalDateTime.now().plusNanos(refreshTokenExpirationMs * 1_000_000);
        Session session = Session.create(token, new UserId(userId), expiredAt, ip, ua);
        authRepository.save(session);
    }

    @Transactional
    public void validateAndRevokeOld(String refreshToken) {
        if (!jwtService.isValid(refreshToken) || jwtService.isTokenExpired(refreshToken)) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }

        Session session = authRepository.findByTokenHash(refreshToken)
                .orElseThrow(SessionNotFoundException::new);

        if (!session.isValid()) {
            authRepository.revokeAllByUserId(session.getUserId());
            throw new TokenRevokedException("Refresh token revoked. Potential reuse detected.");
        }

        session.revoke();
        authRepository.save(session);
    }

    @Transactional
    public void logout(String refreshToken) {
        authRepository.findByTokenHash(refreshToken).ifPresent(session -> {
            authRepository.revokeByDevice(session.getUserId(), session.getIpAddress(), session.getUserAgent());
        });
    }

    @Transactional
    public JwtTokenDTO rotateTokens(String oldRefreshToken, String ip, String ua) {
        validateAndRevokeOld(oldRefreshToken);

        UUID userId = jwtService.getUserIdFromToken(oldRefreshToken);
        String email = jwtService.getEmailFromToken(oldRefreshToken);
        String role = jwtService.getRoleFromToken(oldRefreshToken);

        String newAccessToken = jwtService.generateToken(userId, email, role, JWTType.ACCESS);
        String newRefreshToken = jwtService.generateToken(userId, email, role, JWTType.REFRESH);

        saveSession(userId, newRefreshToken, ip, ua);

        return new JwtTokenDTO(newAccessToken, newRefreshToken);
    }
}
