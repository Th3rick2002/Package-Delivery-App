package com.example.smallbox.user.domain.exceptions;

import com.example.smallbox.shared.domain.exception.ForbiddenException;

public class UserCreationForbiddenException extends ForbiddenException {
    public UserCreationForbiddenException(String message) {
        super("USER_CREATION_FORBIDDEN", message);
    }
}
