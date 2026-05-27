package com.example.smallbox.branch.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record BranchUserResponse(
    Integer branchId,
    UUID userId,
    Boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) { }
