package com.example.smallbox.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "User details response")
public record UserResponse(
        @Schema(description = "User's unique identifier")
        UUID id,

        @Schema(description = "User's full name")
        String fullName,

        @Schema(description = "User's email")
        String email,

        @Schema(description = "User's phone number")
        String phone,

        @Schema(description = "User's assigned role")
        String role
) {}
