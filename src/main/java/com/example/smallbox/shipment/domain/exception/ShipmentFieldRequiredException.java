package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.BadRequestException;

public class ShipmentFieldRequiredException extends BadRequestException {
    public ShipmentFieldRequiredException(String field) {
        super(field.toUpperCase() + "_REQUIRED", field + " is required for the shipment");
    }
}
