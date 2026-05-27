package com.example.smallbox.auth.infrastructure.security.filter;

import com.example.smallbox.auth.infrastructure.security.jwt.JwtService;
import com.example.smallbox.auth.infrastructure.security.service.CustomUserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

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

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtService.getEmailFromToken(token);
        String role = jwtService.getRoleFromToken(token);
        UUID userId = jwtService.getUserIdFromToken(token);

        if (email != null) {
            var authorities = List.of(new SimpleGrantedAuthority(role));
            var principal = new CustomUserPrincipal(userId, email, null, authorities);
            var authtoken = new UsernamePasswordAuthenticationToken(principal, null, authorities);
            authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authtoken);
        }

        filterChain.doFilter(request, response);
    }
}
