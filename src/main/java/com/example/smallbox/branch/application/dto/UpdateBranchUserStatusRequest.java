package com.example.smallbox.branch.application.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateBranchUserStatusRequest(
    @NotNull(message = "El estado activo es obligatorio")
    Boolean active
) { }
