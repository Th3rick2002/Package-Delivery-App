package com.example.smallbox.branch.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Request to create a new branch")
public record CreateBranchRequest(
    @Schema(description = "Name of the branch", example = "Downtown Hub")
    @NotBlank(message = "El nombre de la sucursal no puede estar vacío")
    String name,

    @Schema(description = "Department ID for the branch", example = "5")
    @NotNull
    @Positive
    Integer departmentId,

    @Schema(description = "Branch contact phone", example = "+571234567")
    @NotBlank
    String phone
) { }
