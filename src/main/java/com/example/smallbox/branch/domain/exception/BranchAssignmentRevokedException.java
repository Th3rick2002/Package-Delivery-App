package com.example.smallbox.branch.domain.exception;

import com.example.smallbox.shared.domain.exception.UnauthorizedException;

public class BranchAssignmentRevokedException extends UnauthorizedException {
    public BranchAssignmentRevokedException(String userId) {
        super("BRANCH_ASSIGNMENT_REVOKED", String.format("The user with ID %s is not assigned to a branch or has been revoked.", userId));
    }
}
