package com.example.smallbox.auth.infrastructure.security.service;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

public class StaffUserPrincipal extends CustomUserPrincipal {
    @Getter
    private final Integer branchId;

    public StaffUserPrincipal(UUID userId, String email, String password,
                              Collection<? extends GrantedAuthority> authorities, Integer branchId) {
        super(userId, email, password, authorities);
        this.branchId = branchId;
    }
}
