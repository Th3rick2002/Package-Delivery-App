package com.example.smallbox.branch.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateBranchRequest(
        String name,
        Integer departmentId,
        String phone
) {
}
