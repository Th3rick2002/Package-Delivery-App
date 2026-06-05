package com.example.smallbox.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to create a new user")
public record CreateClientRequest(
        @Schema(description = "User's first name", example = "John")
        @NotBlank String firstName,

        @Schema(description = "User's second name (optional)", example = "Quincy")
        String secondName,

        @Schema(description = "User's last name", example = "Doe")
        @NotBlank String lastName,

        @Schema(description = "User's second last name (optional)", example = "Smith")
        String secondLastName,

        @Schema(description = "User's phone number", example = "+573001234567")
        @NotBlank String phone,

        @Schema(description = "User's email", example = "john.doe@example.com")
        @NotBlank @Email String email,

        @Schema(description = "User's password", example = "Password123!")
        @NotBlank @Size(min = 8) String password
) {}
