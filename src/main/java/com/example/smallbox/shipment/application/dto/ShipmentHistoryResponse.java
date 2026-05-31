package com.example.smallbox.shipment.application.dto;

import com.example.smallbox.shipment.domain.ShipmentHistory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Shipment status history entry")
public record ShipmentHistoryResponse(
        @Schema(description = "Internal history record ID", example = "1")
        Long historyId,

        @Schema(description = "Internal shipment ID", example = "100")
        Long shipmentId,

        @Schema(description = "Internal ID of the status", example = "2")
        int statusId,

        @Schema(description = "Name of the status", example = "RECEIVED_ORIGIN")
        String status,

        @Schema(description = "UUID of the user who made the change", example = "550e8400-e29b-41d4-a716-446655440000")
        String changedBy,

        @Schema(description = "Optional comments about the status change", example = "Package received in good condition")
        String comments,

        @Schema(description = "Timestamp of the status change")
        LocalDateTime createdAt
) {
    public static ShipmentHistoryResponse from(ShipmentHistory history) {
        return new ShipmentHistoryResponse(
                history.historyId(),
                history.shipmentId(),
                history.status().databaseId(),
                history.status().name(),
                history.changedBy().uuid().toString(),
                history.comments(),
                history.createdAt()
        );
    }
}
