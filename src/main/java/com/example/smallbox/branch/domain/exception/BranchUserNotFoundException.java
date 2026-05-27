package com.example.smallbox.branch.domain.exception;

import com.example.smallbox.shared.domain.exception.NotFoundException;

import java.util.UUID;

public class BranchUserNotFoundException extends NotFoundException {
    public BranchUserNotFoundException(Integer branchId, UUID userId) {
        super(
                "BRANCH_USER_NOT_FOUND",
                String.format("User with id %s is not assigned to branch with id %d", userId, branchId)
        );
    }
}
