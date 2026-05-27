package com.example.smallbox.shipment.infrastructure.persistence;

import com.example.smallbox.shipment.domain.Shipment;
import com.example.smallbox.shipment.domain.port.ShipmentRepository;
import com.example.smallbox.shipment.domain.vo.TrackingNumber;
import com.example.smallbox.shipment.infrastructure.persistence.entities.ShipmentJpaEntity;
import com.example.smallbox.shipment.infrastructure.persistence.mapper.ShipmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ShipmentPersistenceAdapter implements ShipmentRepository {
    private final JpaShipmentRepository jpaShipmentRepository;

    @Override
    @Transactional
    public Shipment save(Shipment shipment) {
        ShipmentJpaEntity entity = ShipmentMapper.toJpaEntity(shipment);
        ShipmentJpaEntity saved = jpaShipmentRepository.save(entity);
        return ShipmentMapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Shipment> findById(Long id) {
        return jpaShipmentRepository.findById(id).map(ShipmentMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Shipment> findByTrackingNumber(TrackingNumber trackingNumber) {
        return jpaShipmentRepository.findByTrackingNumber(trackingNumber.value())
                .map(ShipmentMapper::toDomain);
    }

}
