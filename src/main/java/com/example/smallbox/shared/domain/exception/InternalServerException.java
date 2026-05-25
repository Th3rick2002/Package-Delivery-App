package com.example.smallbox.shared.domain.exception;

public abstract class InternalServerException extends DomainException {
    protected InternalServerException(String errorCode, String message) {
        super(errorCode, message);
    }

    @Override
    public int getHttpStatus() {
        return 500;
    }
}
