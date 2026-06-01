package com.example.smallbox.user.domain.exceptions;

import com.example.smallbox.shared.domain.exception.UnprocessableEntityException;

public class UserInvalidRoleException extends UnprocessableEntityException {
    public UserInvalidRoleException(String message) {
        super("USER_INVALID_ROLE", message);
    }
}
