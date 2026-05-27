package com.example.smallbox.user.domain.exceptions;

import com.example.smallbox.shared.domain.exception.NotFoundException;

import java.util.UUID;

public class RoleNotFoundException extends NotFoundException {
    public RoleNotFoundException(Integer id) {
        super("ROLE_NOT_FOUND", "Role with id " + id + " not found");
    }
}
