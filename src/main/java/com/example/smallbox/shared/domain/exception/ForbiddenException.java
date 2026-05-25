package com.example.smallbox.shared.domain.exception;

public abstract class ForbiddenException extends DomainException {
    protected ForbiddenException(String errorCode, String message) {
        super(errorCode, message);
    }

    @Override
    public int getHttpStatus() {
        return 403;
    }
}
