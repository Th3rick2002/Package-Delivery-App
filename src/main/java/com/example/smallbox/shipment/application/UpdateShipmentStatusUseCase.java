package com.example.smallbox.shipment.application;

import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.shipment.domain.Shipment;
import com.example.smallbox.shipment.domain.enums.ShipmentStatus;
import com.example.smallbox.shipment.domain.exception.ShipmentNotFoundException;
import com.example.smallbox.shipment.domain.port.ShipmentRepository;
import com.example.smallbox.shipment.domain.vo.TrackingNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateShipmentStatusUseCase {

    private final ShipmentRepository shipmentRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void execute(String trackingNumber, ShipmentStatus newStatus, UserId changedBy, String comments) {
        Shipment shipment = shipmentRepository.findByTrackingNumber(new TrackingNumber(trackingNumber))
                .orElseThrow(() -> new ShipmentNotFoundException(trackingNumber));

        shipment.changeStatus(newStatus, changedBy, comments);
        shipmentRepository.save(shipment);

        shipment.pullDomainEvents().forEach(eventPublisher::publishEvent);
    }
}
