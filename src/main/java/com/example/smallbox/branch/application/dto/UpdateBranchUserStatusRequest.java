package com.example.smallbox.branch.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Request to update user status in a branch")
public record UpdateBranchUserStatusRequest(
    @Schema(description = "ID of the branch to assign")
    @NotNull(message = "El ID de la sucursal es obligatorio")
    Integer branchId,

    @Schema(description = "ID of the user to assign")
    @NotNull(message = "El ID de usuario es obligatorio")
    UUID userId,

    @Schema(description = "Active status", example = "true")
    @NotNull(message = "El estado activo es obligatorio")
    Boolean active
) { }
