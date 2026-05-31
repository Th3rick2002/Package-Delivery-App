package com.example.smallbox.branch.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to update user status in a branch")
public record UpdateBranchUserStatusRequest(
    @Schema(description = "Active status", example = "true")
    @NotNull(message = "El estado activo es obligatorio")
    Boolean active
) { }
