package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.NotFoundException;

import java.util.UUID;

public class ShipmentNotFoundException extends NotFoundException {
    public ShipmentNotFoundException(UUID id) {
        super("SHIPMENT_NOT_FOUND", "Shipment with id " + id + " not found");
    }
}
