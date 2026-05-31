package com.example.smallbox.shipment.domain.enums;

import com.example.smallbox.shipment.domain.exception.ShipmentValidationException;

public enum ShipmentStatus {
    CREATED(1),
    RECEIVED_ORIGIN(2),
    IN_TRANSIT(3),
    RECEIVED_DESTINATION(4),
    DELIVERED(5),
    CANCELLED(6);

    private final int databaseId;

    ShipmentStatus(int databaseId) {
        this.databaseId = databaseId;
    }

    public int databaseId() {
        return databaseId;
    }

    public static ShipmentStatus fromDatabaseId(Integer databaseId) {
        if (databaseId == null) {
            throw new ShipmentValidationException("SHIPMENT_STATUS_REQUIRED", "Shipment status id is required");
        }

        for (ShipmentStatus status : values()) {
            if (status.databaseId == databaseId) {
                return status;
            }
        }

        throw new ShipmentValidationException(
                "UNKNOWN_SHIPMENT_STATUS",
                "Unknown shipment status id " + databaseId
        );
    }
}
