package com.example.smallbox.shared.domain.exception;

public abstract class UnprocessableEntityException extends DomainException {
    protected UnprocessableEntityException(String errorCode, String message) {
        super(errorCode, message);
    }

    @Override
    public int getHttpStatus() {
        return 422;
    }
}
