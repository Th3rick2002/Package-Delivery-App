package com.example.smallbox.branch.domain.exception;

import com.example.smallbox.shared.domain.exception.UnprocessableEntityException;

public class InvalidRoleForBranchException extends UnprocessableEntityException {
    public InvalidRoleForBranchException(String roleName) {
        super(
                "INVALID_ROLE_FOR_BRANCH",
                String.format("Role %s is not allowed to be assigned to a branch", roleName)
        );
    }
}
