package com.example.smallbox.user.domain.exceptions;

import com.example.smallbox.shared.domain.exception.ConflictException;

public class EmailAlreadyInUseException extends ConflictException {
    public EmailAlreadyInUseException(String email) {
        super("EMAIL_ALREADY_IN_USE", "Email " + email + " is already in use");
    }
}
