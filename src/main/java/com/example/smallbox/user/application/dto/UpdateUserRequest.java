package com.example.smallbox.user.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    String firstName,

    String secondName,

    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    String lastName,

    String secondLastName,

    @Size(min = 8, max = 20, message = "Phone must be between 8 and 20 characters")
    String phone,

    @Email(message = "Invalid email format")
    String email,

    @Size(min = 6, message = "Password must be at least 6 characters")
    String password
) {}
