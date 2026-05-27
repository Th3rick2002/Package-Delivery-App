package com.example.smallbox.auth.domain.exception;

import com.example.smallbox.shared.domain.exception.NotFoundException;

public class SessionNotFoundException extends NotFoundException {
    public SessionNotFoundException() {
        super("SESSION_NOT_FOUND", "Session not found");
    }
}
