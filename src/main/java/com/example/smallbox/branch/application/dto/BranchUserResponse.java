package com.example.smallbox.branch.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Branch user assignment response")
public record BranchUserResponse(
    @Schema(description = "ID of the branch")
    Integer branchId,

    @Schema(description = "ID of the user")
    UUID userId,

    @Schema(description = "Whether the user is currently active in this branch")
    Boolean active,

    @Schema(description = "Timestamp when the assignment was created")
    LocalDateTime createdAt,

    @Schema(description = "Timestamp when the assignment was last updated")
    LocalDateTime updatedAt
) { }
