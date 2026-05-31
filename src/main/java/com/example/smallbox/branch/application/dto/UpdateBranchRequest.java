package com.example.smallbox.branch.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Request to update an existing branch")
public record UpdateBranchRequest(
        @Schema(description = "New name of the branch", example = "Updated Downtown Hub")
        String name,

        @Schema(description = "New department ID", example = "6")
        Integer departmentId,

        @Schema(description = "New contact phone", example = "+577654321")
        String phone
) {
}
