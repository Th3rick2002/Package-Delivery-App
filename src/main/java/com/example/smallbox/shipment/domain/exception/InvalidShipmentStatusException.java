package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.UnprocessableEntityException;
import com.example.smallbox.shipment.domain.enums.ShipmentStatus;

public class InvalidShipmentStatusException extends UnprocessableEntityException {
    public InvalidShipmentStatusException(ShipmentStatus status) {
        super("INVALID_SHIPMENT_STATUS", "Action not allowed for shipment in status " + status);
    }

    public InvalidShipmentStatusException(ShipmentStatus from, ShipmentStatus to) {
        super("INVALID_SHIPMENT_STATUS", "Cannot transition shipment from " + from + " to " + to);
    }
}
