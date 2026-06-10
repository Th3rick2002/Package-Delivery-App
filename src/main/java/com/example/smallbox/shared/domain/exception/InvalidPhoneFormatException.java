package com.example.smallbox.shared.domain.exception;

public class InvalidPhoneFormatException extends BadRequestException {
    public InvalidPhoneFormatException(String value) {
        super("INVALID_PHONE_FORMAT", "Invalid phone number format: " + value + ". Must be 8 digits (optionally prefixed with +)");
    }
}
