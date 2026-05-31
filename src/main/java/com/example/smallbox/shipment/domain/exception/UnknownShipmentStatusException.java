package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.BadRequestException;

public class UnknownShipmentStatusException extends BadRequestException {
    public UnknownShipmentStatusException(Integer id) {
        super("UNKNOWN_SHIPMENT_STATUS", "Unknown shipment status id: " + id);
    }
}
