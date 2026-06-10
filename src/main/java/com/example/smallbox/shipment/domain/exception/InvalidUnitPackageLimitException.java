package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.BadRequestException;

public class InvalidUnitPackageLimitException extends BadRequestException {
    public InvalidUnitPackageLimitException(String unit, int limit) {
        super("INVALID_PACKAGE_DIMENSIONS", "The package limit for " + unit + " is " + limit + ".");
    }
}
