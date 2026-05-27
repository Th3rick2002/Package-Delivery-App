package com.example.smallbox.auth.domain.exception;

import com.example.smallbox.shared.domain.exception.UnauthorizedException;

public class TokenRevokedException extends UnauthorizedException {
    public TokenRevokedException(String reason) {
        super("TOKEN_REVOKED", reason);
    }
}
