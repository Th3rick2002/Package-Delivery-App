package com.example.smallbox.branch.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Branch details response")
public record BranchResponse(
        @Schema(description = "Branch unique identifier")
        Integer id,

        @Schema(description = "Branch name", example = "North Station")
        String nameBranch,

        @Schema(description = "Department ID where the branch is located", example = "1")
        Integer departmentID,

        @Schema(description = "Branch phone number", example = "555-1234")
        String phone,

        @Schema(description = "Timestamp when the branch was created")
        LocalDateTime createdAt,

        @Schema(description = "Timestamp when the branch was last updated")
        LocalDateTime updatedAt
) {
}
