package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.BadRequestException;

public class InvalidPackageDescriptionException extends BadRequestException {
    public InvalidPackageDescriptionException() {
        super("PACKAGE_DESCRIPTION_REQUIRED", "Package description is required and cannot be blank");
    }
}
