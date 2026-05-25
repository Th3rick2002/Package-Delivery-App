package com.example.smallbox.shared.domain.exception;

public abstract class ServiceUnavailableException extends DomainException {
    protected ServiceUnavailableException(String errorCode, String message) {
        super(errorCode, message);
    }

    @Override
    public int getHttpStatus() {
        return 503;
    }
}
