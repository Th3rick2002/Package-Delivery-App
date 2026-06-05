package com.example.smallbox.auth.application;

import com.example.smallbox.auth.infrastructure.security.jwt.JwtService;
import com.example.smallbox.auth.infrastructure.security.jwt.JWTType;
import com.example.smallbox.auth.infrastructure.security.service.CustomUserPrincipal;
import com.example.smallbox.auth.infrastructure.security.service.StaffUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtService jwtService;

    public String generateAccessToken(CustomUserPrincipal principal) {
        String role = principal.getAuthorities().iterator().next().getAuthority();
        return jwtService.generateToken(principal.getUserId(), principal.getUsername(), role, JWTType.ACCESS);
    }

    public String generateInternalAccessToken(StaffUserPrincipal principal) {
        String role = principal.getAuthorities().iterator().next().getAuthority();
        return jwtService.generateInternalToken(principal.getUserId(), principal.getUsername(), principal.getBranchId(), role, JWTType.ACCESS);
    }

    public String generateRefreshToken(CustomUserPrincipal principal) {
        String role = principal.getAuthorities().iterator().next().getAuthority();
        return jwtService.generateToken(principal.getUserId(), principal.getUsername(), role, JWTType.REFRESH);
    }

    public String generateAccessTokenFromData(UUID userId, String email, String role) {
        return jwtService.generateToken(userId, email, role, JWTType.ACCESS);
    }
}
