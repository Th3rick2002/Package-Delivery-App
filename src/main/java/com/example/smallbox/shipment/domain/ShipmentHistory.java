package com.example.smallbox.shipment.domain;

import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.shipment.domain.enums.ShipmentStatus;

import java.time.LocalDateTime;

public record ShipmentHistory(
        Long historyId,
        Long shipmentId,
        ShipmentStatus status,
        UserId changedBy,
        String comments,
        LocalDateTime createdAt
) {
    public static ShipmentHistory create(Long shipmentId, ShipmentStatus status, UserId changedBy, String comments) {
        return new ShipmentHistory(null, shipmentId, status, changedBy, comments, LocalDateTime.now());
    }
}
