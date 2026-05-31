package com.example.smallbox.shipment.application.dto;

import com.example.smallbox.shipment.domain.enums.ShipmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to update the status of a shipment")
public record UpdateShipmentStatusRequest(
        @Schema(description = "The new status to assign to the shipment", example = "IN_TRANSIT")
        @NotNull(message = "New status is required") ShipmentStatus newStatus,

        @Schema(description = "Optional comments or reason for the status update", example = "Departed from main hub")
        String comments
) {}
