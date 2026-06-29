package com.example.smallbox.auth.infrastructure.web;

import com.example.smallbox.auth.application.AuthService;
import com.example.smallbox.auth.application.RefreshTokenService;
import com.example.smallbox.auth.application.dto.LoginRequest;
import com.example.smallbox.auth.infrastructure.security.jwt.JwtTokenDTO;
import com.example.smallbox.shared.application.dto.ApiErrorResponse;
import com.example.smallbox.user.application.UserService;
import com.example.smallbox.user.application.dto.CreateClientRequest;
import com.example.smallbox.user.application.dto.UserResponse;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Endpoints for user authentication and session management")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserService userService;

    @Value("${jwt.cookie.secure:false}")
    private boolean secureCookie;

    @Value("${jwt.expiration-time-ms}")
    private long accessTokenMs;

    @Value("${jwt.expiration-time-refresh-ms}")
    private long refreshTokenMs;

    private static final String BLACKLIST_PREFIX = "jwt:blacklist:";

    @Operation(
            summary = "Login for clients",
            description = "Authenticates a clients and sets 'accessToken' and 'refreshToken' cookies.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login successful",
                            headers = {
                                    @Header(name = "Set-Cookie", description = "Access and Refresh tokens")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid credentials",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse response
    ) {
        String ip = extractIp(httpRequest);
        String ua = httpRequest.getHeader("User-Agent");
        
        JwtTokenDTO tokens = authService.login(request, ip, ua);
        
        setTokenCookie("accessToken", tokens.accessToken(), accessTokenMs / 1000, "/", response);
        setTokenCookie("refreshToken", tokens.refreshToken(), refreshTokenMs / 1000, "/api/v1/auth/refresh", response);

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Login for staff",
            description = "Authenticates a staff user and sets 'accessToken' and 'refreshToken' cookies.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login successful",
                            headers = {
                                    @Header(name = "Set-Cookie", description = "Access and Refresh tokens")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid credentials",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @PostMapping("/private/login")
    public ResponseEntity<?> privateLogin(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse response
    ) {
        String ip = extractIp(httpRequest);
        String ua = httpRequest.getHeader("User-Agent");

        JwtTokenDTO tokens = authService.loginAdministrative(request, ip, ua);

        setTokenCookie("accessToken", tokens.accessToken(), accessTokenMs / 1000, "/", response);
        setTokenCookie("refreshToken", tokens.refreshToken(), refreshTokenMs / 1000, "/api/v1/auth/refresh", response);

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Register a new client",
            description = "Registers a new client with the system.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User registered successfully",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Email already exist",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateClientRequest request) {
        UserResponse response = userService.registerClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Refresh access token",
            description = "Uses the 'refreshToken' cookie to issue a new set of tokens.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tokens rotated successfully",
                            headers = {
                                    @Header(name = "Set-Cookie", description = "New Access and Refresh tokens")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid or missing refresh token",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
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
        
        setTokenCookie("accessToken", tokens.accessToken(), accessTokenMs / 1000, "/", response);
        setTokenCookie("refreshToken", tokens.refreshToken(), refreshTokenMs / 1000, "/api/v1/auth/refresh", response);

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Logout from the system",
            description = "Revokes the session and clears authentication cookies.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Logout successful")
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletRequest httpRequest,
            HttpServletResponse response
    ) {
        if (refreshToken != null) {
            refreshTokenService.logout(refreshToken);
        }

        String accessToken = null;
        if (httpRequest.getCookies() != null) {
            accessToken = Arrays.stream(httpRequest.getCookies())
                    .filter(cookie -> "accessToken".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }

        if (accessToken != null && !accessToken.isBlank()) {
            long remainingTimeMs = accessTokenMs;

            if (remainingTimeMs > 0) {
                redisTemplate.opsForValue().set(
                        BLACKLIST_PREFIX + accessToken,
                        "revoked",
                        Duration.ofMillis(remainingTimeMs)
                );
                log.info("Token de acceso ingresado a la Lista Negra perimetral en Redis de forma exitosa.");
            }
        }

        org.springframework.security.core.context.SecurityContextHolder.clearContext();
        
        clearTokenCookie("accessToken", "/", response);
        clearTokenCookie("refreshToken", "/api/v1/auth/refresh", response);
        
        return ResponseEntity.ok().build();
    }

    private void setTokenCookie(String name, String token, long maxAge, String path, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(name, token)
                .httpOnly(true)
                .secure(secureCookie)
                .path(path)
                .maxAge(maxAge)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    private void clearTokenCookie(String name, String path, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(secureCookie)
                .path(path)
                .maxAge(0)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    private String extractIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        return (ip != null) ? ip.split(",")[0] : request.getRemoteAddr();
    }
}
