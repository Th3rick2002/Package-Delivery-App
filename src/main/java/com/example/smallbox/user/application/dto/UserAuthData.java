package com.example.smallbox.user.application.dto;

import java.util.UUID;

public record UserAuthData(
        UUID id,
        String email,
        String hashPassword,
        String role
) {}
