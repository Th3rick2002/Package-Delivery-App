package com.example.smallbox.shipment.infrastructure.persistence;

import com.example.smallbox.shipment.infrastructure.persistence.entities.ShipmentHistoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaShipmentHistoryRepository extends JpaRepository<ShipmentHistoryJpaEntity, Long> {
    List<ShipmentHistoryJpaEntity> findByShipmentIdOrderByCreatedAtAsc(Long shipmentId);
}
