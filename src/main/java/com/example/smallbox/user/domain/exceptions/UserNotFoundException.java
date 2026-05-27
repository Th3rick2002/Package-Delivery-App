package com.example.smallbox.user.domain.exceptions;

import com.example.smallbox.shared.domain.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String identifier) {
        super(
                "USER_NOT_FOUND",
                "User with identifier " + identifier + " not found"
        );
    }
}
