package com.example.smallbox.shipment.application;

import com.example.smallbox.shipment.application.dto.ShipmentResponse;
import com.example.smallbox.shipment.domain.Shipment;
import com.example.smallbox.shipment.domain.exception.ShipmentNotFoundException;
import com.example.smallbox.shipment.domain.port.ShipmentRepository;
import com.example.smallbox.shipment.domain.vo.TrackingNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetShipmentUseCase {

    private final ShipmentRepository shipmentRepository;

    @Cacheable(value = "shipments", key = "#trackingNumber")
    @Transactional(readOnly = true)
    public ShipmentResponse execute(String trackingNumber) {
        Shipment shipment = shipmentRepository.findByTrackingNumber(new TrackingNumber(trackingNumber))
                .orElseThrow(() -> new ShipmentNotFoundException(trackingNumber));
        return ShipmentResponse.from(shipment);
    }
}
