package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.BadRequestException;

public class ShipmentValidationException extends BadRequestException {
    public ShipmentValidationException(String errorCode, String message) {
        super(errorCode, message);
    }
}
