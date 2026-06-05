package com.example.smallbox.branch.domain.exception;

import com.example.smallbox.shared.domain.exception.NotFoundException;

import java.util.UUID;

public class BranchUserNotAssignedException extends NotFoundException {
    public BranchUserNotAssignedException(UUID userId) {
        super(
                "BRANCH_USER_NOT_ASSIGNED",
                String.format("User with id %s is not assigned to any branch", userId)
        );
    }
}
