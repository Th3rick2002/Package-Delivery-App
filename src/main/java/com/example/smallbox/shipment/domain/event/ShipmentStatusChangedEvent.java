package com.example.smallbox.shipment.domain.event;

import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.shipment.domain.enums.ShipmentStatus;

import java.time.LocalDateTime;

public record ShipmentStatusChangedEvent(
        Long shipmentId,
        ShipmentStatus oldStatus,
        ShipmentStatus newStatus,
        UserId changedBy,
        String comments,
        LocalDateTime changedAt
) {}
