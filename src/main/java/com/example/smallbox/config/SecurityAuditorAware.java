package com.example.smallbox.config;

import com.example.smallbox.auth.infrastructure.security.service.CustomUserPrincipal;
import com.example.smallbox.auth.infrastructure.security.service.StaffUserPrincipal;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SecurityAuditorAware implements AuditorAware<UUID> {
    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getName()))
        {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserPrincipal userPrincipal) {
            return Optional.of(userPrincipal.getUserId());
        }

        if (principal instanceof StaffUserPrincipal staffUserPrincipal) {
            return Optional.of(staffUserPrincipal.getUserId());
        }

        return Optional.empty();
    }
}
