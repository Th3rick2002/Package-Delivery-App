package com.example.smallbox.auth.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request to authenticate a user")
public record LoginRequest(
        @Schema(description = "User email", example = "admin@smallbox.com")
        @NotBlank @Email String email,

        @Schema(description = "User password", example = "SecurePass123")
        @NotBlank String password
) {}
