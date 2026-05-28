package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.NotFoundException;

public class ShipmentNotFoundException extends NotFoundException {
    public ShipmentNotFoundException(Long id) {
        super("SHIPMENT_NOT_FOUND", "Shipment with id " + id + " not found");
    }
}
