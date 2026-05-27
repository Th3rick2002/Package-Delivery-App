package com.example.smallbox.shipment.infrastructure.persistence;

import com.example.smallbox.shipment.infrastructure.persistence.entities.ShipmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface JpaShipmentRepository extends JpaRepository<ShipmentJpaEntity, Long> {
    Optional<ShipmentJpaEntity> findByTrackingNumber(String trackingNumber);
}
