package com.example.smallbox.auth.infrastructure.web;

import com.example.smallbox.auth.application.AuthService;
import com.example.smallbox.auth.application.RefreshTokenService;
import com.example.smallbox.auth.application.dto.LoginRequest;
import com.example.smallbox.auth.infrastructure.security.jwt.JwtTokenDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.cookie.secure:false}")
    private boolean secureCookie;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse response
    ) {
        String ip = extractIp(httpRequest);
        String ua = httpRequest.getHeader("User-Agent");
        
        JwtTokenDTO tokens = authService.login(request, ip, ua);
        setTokenCookie(tokens.refreshToken(), response);

        return ResponseEntity.ok(Map.of("token", tokens.accessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletRequest httpRequest,
            HttpServletResponse response
    ) {
        if (refreshToken == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Refresh token missing"));
        }

        String ip = extractIp(httpRequest);
        String ua = httpRequest.getHeader("User-Agent");

        JwtTokenDTO tokens = refreshTokenService.rotateTokens(refreshToken, ip, ua);
        setTokenCookie(tokens.refreshToken(), response);

        return ResponseEntity.ok(Map.of("token", tokens.accessToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        if (refreshToken != null) {
            refreshTokenService.logout(refreshToken);
        }
        return ResponseEntity.ok().build();
    }

    private void setTokenCookie(String token, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(secureCookie)
                .path("/api/v1/auth/refresh")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    private String extractIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        return (ip != null) ? ip.split(",")[0] : request.getRemoteAddr();
    }
}
