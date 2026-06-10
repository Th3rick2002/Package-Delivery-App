package com.example.smallbox.shipment.application.dto;

import com.example.smallbox.shipment.domain.Shipment;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Lightweight shipment summary for pagination")
public record ShipmentSummaryResponse(
        @Schema(description = "Shipment ID", example = "1")
        Long shipmentId,

        @Schema(description = "Unique tracking number for the shipment", example = "SB-123456789")
        String trackingNumber,

        @Schema(description = "Current status of the shipment", example = "PENDING")
        String status,

        @Schema(description = "Total cost of the shipment", example = "25.50")
        BigDecimal totalPrice,

        @Schema(description = "Currency of the price", example = "USD")
        String currency,

        @Schema(description = "ID of the sender user")
        UUID senderId,

        @Schema(description = "ID of the destination city")
        Integer destinationCityId,

        @Schema(description = "ID of the origin branch")
        Integer originBranchId,

        @Schema(description = "ID of the destination branch")
        Integer destinationBranchId,

        @Schema(description = "Total number of packages in the shipment", example = "2")
        Integer packageCount,

        @Schema(description = "Timestamp when the shipment was created")
        LocalDateTime createdAt
) {
    public static ShipmentSummaryResponse from(Shipment shipment) {
        return new ShipmentSummaryResponse(
                shipment.shipmentId(),
                shipment.trackingNumber().value(),
                shipment.status().name(),
                shipment.totalPrice().amount(),
                shipment.totalPrice().currency(),
                shipment.senderId().uuid(),
                shipment.destinationCityId().cityId(),
                shipment.originBranchId().id(),
                shipment.destinationBranchId().id(),
                shipment.packages().size(),
                shipment.createdAt()
        );
    }
}
