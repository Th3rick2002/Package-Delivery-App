package com.example.smallbox.auth.infrastructure.security.filter;

import com.example.smallbox.auth.infrastructure.security.jwt.JwtService;
import com.example.smallbox.auth.infrastructure.security.service.CustomUserPrincipal;
import com.example.smallbox.auth.infrastructure.security.service.StaffUserPrincipal;
import com.example.smallbox.auth.infrastructure.security.service.TokenBlacklistVerifier;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final TokenBlacklistVerifier tokenBlacklistVerifier;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = null;

        if (request.getCookies() != null) {
            token = Arrays.stream(request.getCookies())
                    .filter(cookie -> "accessToken".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtService.isValid(token) || jwtService.isTokenExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (tokenBlacklistVerifier.isTokenRevoked(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error_code\": \"UNAUTHORIZED\", \"message\": \"Sesión inválida o cerrada previamente.\"}");
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtService.getEmailFromToken(token);
        String role = jwtService.getRoleFromToken(token);
        UUID userId = jwtService.getUserIdFromToken(token);

        UserDetails userDetails;

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var authorities = List.of(new SimpleGrantedAuthority(role));

            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENT"))) {
                userDetails = new CustomUserPrincipal(userId, email, null, authorities);
            } else {
                Integer branchId = jwtService.getBranchIdFromToken(token);
                userDetails = new StaffUserPrincipal(userId, email,null, authorities, branchId );
            }

            var authtoken = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authtoken);
            MDC.put("userId", authtoken.getName());
        }

        filterChain.doFilter(request, response);
    }
}
