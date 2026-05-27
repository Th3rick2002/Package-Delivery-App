package com.example.smallbox.branch.domain.exception;

import com.example.smallbox.shared.domain.exception.ConflictException;

import java.util.UUID;

public class BranchUserAlreadyExistsException extends ConflictException {
    public BranchUserAlreadyExistsException(Integer branchId, UUID userId) {
        super(
                "BRANCH_USER_ALREADY_EXISTS",
                String.format("User with id %s is already assigned to branch with id %d", userId, branchId)
        );
    }
}
