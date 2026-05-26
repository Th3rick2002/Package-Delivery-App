package com.example.smallbox.branch.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateBranchRequest(
    @NotBlank(message = "El nombre de la sucursal no puede estar vacío")
    String name,

    @NotNull
    @Positive
    Integer departmentId,

    @NotBlank
    String phone
) { }
