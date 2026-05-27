package com.example.smallbox.auth.domain.exception;

import com.example.smallbox.shared.domain.exception.UnauthorizedException;

public class InvalidTokenException extends UnauthorizedException {
    public InvalidTokenException() {
        super("INVALID_TOKEN", "Invalid or expired token");
    }

    public InvalidTokenException(String message) {
        super("INVALID_TOKEN", message);
    }
}
