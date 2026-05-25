package com.example.smallbox.branch.application.dto;

import java.time.LocalDateTime;

public record BranchResponse(
        Integer id,
        String nameBranch,
        Integer departmentID,
        String phone,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
