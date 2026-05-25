package com.example.smallbox.shared.domain.exception;

public abstract class UnauthorizedException extends DomainException {
    protected UnauthorizedException(String errorCode, String message) {
        super(errorCode, message);
    }

    @Override
    public int getHttpStatus() {
        return 401;
    }
}
