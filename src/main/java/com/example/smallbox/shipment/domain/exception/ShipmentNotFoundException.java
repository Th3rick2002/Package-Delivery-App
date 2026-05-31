package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.NotFoundException;

public class ShipmentNotFoundException extends NotFoundException {
    public ShipmentNotFoundException(Long id) {
        super("SHIPMENT_NOT_FOUND", "Shipment with id " + id + " not found");
    }

    public ShipmentNotFoundException(String trackingNumber) {
        super("TRACKING_NUMBER_NOT_FOUND", "Shipment with tracking number " + trackingNumber + " not found");
    }
}
