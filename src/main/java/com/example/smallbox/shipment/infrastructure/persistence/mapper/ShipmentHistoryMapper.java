package com.example.smallbox.shipment.infrastructure.persistence.mapper;

import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.shipment.domain.ShipmentHistory;
import com.example.smallbox.shipment.domain.enums.ShipmentStatus;
import com.example.smallbox.shipment.infrastructure.persistence.entities.ShipmentHistoryJpaEntity;

public class ShipmentHistoryMapper {

    private ShipmentHistoryMapper() {}

    public static ShipmentHistoryJpaEntity toJpaEntity(ShipmentHistory history) {
        return new ShipmentHistoryJpaEntity(
                history.shipmentId(),
                history.status().databaseId(),
                history.changedBy().uuid(),
                history.comments(),
                history.createdAt()
        );
    }

    public static ShipmentHistory toDomain(ShipmentHistoryJpaEntity entity) {
        return new ShipmentHistory(
                entity.getHistoryId(),
                entity.getShipmentId(),
                ShipmentStatus.fromDatabaseId(entity.getStatusId()),
                new UserId(entity.getChangedBy()),
                entity.getComments(),
                entity.getCreatedAt()
        );
    }
}
