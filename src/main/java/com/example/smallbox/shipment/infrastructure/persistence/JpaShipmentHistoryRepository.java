package com.example.smallbox.shipment.infrastructure.persistence;

import com.example.smallbox.shipment.infrastructure.persistence.entities.ShipmentHistoryJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaShipmentHistoryRepository extends JpaRepository<ShipmentHistoryJpaEntity, Long> {
    Page<ShipmentHistoryJpaEntity> findByShipmentIdOrderByCreatedAtAsc(Long shipmentId, Pageable pageable);
}
