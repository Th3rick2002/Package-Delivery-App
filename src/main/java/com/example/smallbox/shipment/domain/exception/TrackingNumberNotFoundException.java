package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.NotFoundException;

public class TrackingNumberNotFoundException extends NotFoundException {
    public TrackingNumberNotFoundException(String trackingNumber) {
        super("TRACKING_NUMBER_NOT_FOUND", "No shipment found with tracking number " + trackingNumber);
    }
}
