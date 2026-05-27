package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.NotFoundException;

import java.util.UUID;

public class PackageNotFoundException extends NotFoundException {
    public PackageNotFoundException(UUID packageId) {
        super("PACKAGE_NOT_FOUND", "Package with id " + packageId + " not found in the shipment");
    }
}
