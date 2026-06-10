package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.BadRequestException;

public class InvalidPackageLimitException extends BadRequestException {
    public InvalidPackageLimitException(int limit) {
        super("INVALID_PACKAGE_LIMIT", "The package limit is " + limit);
    }
}
