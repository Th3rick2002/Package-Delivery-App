package com.example.smallbox.user.application.dto;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String fullName,
        String email,
        String phone,
        String role
) {}
