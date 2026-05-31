package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.BadRequestException;

public class InvalidTrackingNumberException extends BadRequestException {
    public InvalidTrackingNumberException(String trackingNumber) {
        super("INVALID_TRACKING_NUMBER", "Invalid tracking number format: " + trackingNumber);
    }
}
