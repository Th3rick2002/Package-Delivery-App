package com.example.smallbox.user.domain.exceptions;

import com.example.smallbox.shared.domain.exception.NotFoundException;

public class RoleNotFoundException extends NotFoundException {
    public RoleNotFoundException(Object identifier) {
        super("ROLE_NOT_FOUND", "Role " + identifier + " not found");
    }
}
