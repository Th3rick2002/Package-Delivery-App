package com.example.smallbox.shared.domain.exception;

public abstract class ConflictException extends DomainException {
    protected ConflictException(String errorCode, String message) {
        super(errorCode, message);
    }

    @Override
    public int getHttpStatus() {
        return 409;
    }
}
