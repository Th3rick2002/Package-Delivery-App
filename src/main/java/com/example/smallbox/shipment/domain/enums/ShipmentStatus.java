package com.example.smallbox.shipment.domain.enums;

import com.example.smallbox.shipment.domain.exception.ShipmentFieldRequiredException;
import com.example.smallbox.shipment.domain.exception.UnknownShipmentStatusException;

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
            throw new ShipmentFieldRequiredException("SHIPMENT_STATUS_ID");
        }

        for (ShipmentStatus status : values()) {
            if (status.databaseId == databaseId) {
                return status;
            }
        }

        throw new UnknownShipmentStatusException(databaseId);
    }
}
