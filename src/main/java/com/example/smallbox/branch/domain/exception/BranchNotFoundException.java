package com.example.smallbox.branch.domain.exception;

import com.example.smallbox.shared.domain.exception.NotFoundException;

public class BranchNotFoundException extends NotFoundException {
    public BranchNotFoundException(Integer id) {
        super(
                "BRANCH_NOT_FOUND",
                "Branch with id " + id + " not found"
        );
    }
}
