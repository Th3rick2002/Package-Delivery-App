package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.BadRequestException;

public class PackageRequiredException extends BadRequestException {
    public PackageRequiredException() {
        super("PACKAGE_REQUIRED", "At least one package is required for the shipment");
    }
}
