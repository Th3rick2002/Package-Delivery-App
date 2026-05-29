package com.example.smallbox.shipment.application.dto;

import com.example.smallbox.shipment.domain.enums.ShipmentStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateShipmentStatusRequest(
        @NotNull(message = "New status is required") ShipmentStatus newStatus,
        String comments
) {}
