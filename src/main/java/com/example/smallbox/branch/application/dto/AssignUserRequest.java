package com.example.smallbox.branch.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Schema(description = "Request to assign a user to a branch")
public record AssignUserRequest(
    @Schema(description = "ID of the user to assign")
    @NotNull(message = "El ID de usuario es obligatorio")
    UUID userId
) { }
