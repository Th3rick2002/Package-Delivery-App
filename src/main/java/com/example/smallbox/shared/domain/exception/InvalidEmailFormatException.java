package com.example.smallbox.shared.domain.exception;

public class InvalidEmailFormatException extends BadRequestException {
    public InvalidEmailFormatException(String value) {
        super("INVALID_EMAIL_FORMAT", "Invalid email format: " + value);
    }
}
