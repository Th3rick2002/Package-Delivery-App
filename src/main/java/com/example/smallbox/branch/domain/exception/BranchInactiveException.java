package com.example.smallbox.branch.domain.exception;

import com.example.smallbox.shared.domain.exception.UnprocessableEntityException;

public class BranchInactiveException extends UnprocessableEntityException {
    public BranchInactiveException(Integer branchId) {
        super(
                "BRANCH_INACTIVE",
                String.format("Branch with id %d is inactive or deleted", branchId)
        );
    }
}
