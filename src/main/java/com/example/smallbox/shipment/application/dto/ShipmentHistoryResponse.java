package com.example.smallbox.shipment.application.dto;

import com.example.smallbox.shipment.domain.ShipmentHistory;

import java.time.LocalDateTime;

public record ShipmentHistoryResponse(
        Long historyId,
        Long shipmentId,
        int statusId,
        String status,
        String changedBy,
        String comments,
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
