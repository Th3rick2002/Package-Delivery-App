package com.example.smallbox.shared.domain.exception;

public abstract class BadRequestException extends DomainException {
    protected BadRequestException(String errorCode, String message) {
        super(errorCode, message);
    }

    @Override
    public int getHttpStatus() {
        return 400;
    }
}
