package com.example.smallbox.shipment.infrastructure.persistence;

import com.example.smallbox.shipment.domain.ShipmentHistory;
import com.example.smallbox.shipment.domain.port.ShipmentHistoryRepository;
import com.example.smallbox.shipment.infrastructure.persistence.entities.ShipmentHistoryJpaEntity;
import com.example.smallbox.shipment.infrastructure.persistence.mapper.ShipmentHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShipmentHistoryPersistenceAdapter implements ShipmentHistoryRepository {

    private final JpaShipmentHistoryRepository jpaRepository;

    @Override
    @Transactional
    public ShipmentHistory save(ShipmentHistory history) {
        ShipmentHistoryJpaEntity entity = ShipmentHistoryMapper.toJpaEntity(history);
        ShipmentHistoryJpaEntity saved = jpaRepository.save(entity);
        return ShipmentHistoryMapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipmentHistory> findByShipmentId(Long shipmentId) {
        return jpaRepository.findByShipmentIdOrderByCreatedAtAsc(shipmentId).stream()
                .map(ShipmentHistoryMapper::toDomain)
                .toList();
    }
}
