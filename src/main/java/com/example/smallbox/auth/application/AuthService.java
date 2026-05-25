package com.example.smallbox.auth.application;

import com.example.smallbox.auth.application.dto.LoginRequest;
import com.example.smallbox.auth.infrastructure.security.jwt.JwtTokenDTO;
import com.example.smallbox.auth.infrastructure.security.service.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    public JwtTokenDTO login(LoginRequest request, String ip, String ua) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();

        String accessToken = tokenService.generateAccessToken(principal);
        String refreshToken = tokenService.generateRefreshToken(principal);

        refreshTokenService.saveSession(principal.getUserId(), refreshToken, ip, ua);

        return new JwtTokenDTO(accessToken, refreshToken);
    }
}
