package com.example.smallbox.shared.domain.exception;

public abstract class NotFoundException extends DomainException {
    protected NotFoundException(String errorCode, String message) {
        super(errorCode, message);
    }

    @Override
    public int getHttpStatus() {
        return 404;
    }
}
