package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.BadRequestException;

public class InvalidTrackingPrefixException extends BadRequestException {
    public InvalidTrackingPrefixException(String prefix) {
        super("INVALID_TRACKING_PREFIX", "Invalid tracking prefix: " + prefix + ". Must be 2-4 letters");
    }
}
