package com.example.smallbox.shipment.application;

import com.example.smallbox.shared.application.dto.PaginatedMeta;
import com.example.smallbox.shared.application.dto.PaginatedResponse;
import com.example.smallbox.shipment.application.dto.ShipmentHistoryResponse;
import com.example.smallbox.shipment.domain.Shipment;
import com.example.smallbox.shipment.domain.ShipmentHistory;
import com.example.smallbox.shipment.domain.exception.ShipmentNotFoundException;
import com.example.smallbox.shipment.domain.port.ShipmentHistoryRepository;
import com.example.smallbox.shipment.domain.port.ShipmentRepository;
import com.example.smallbox.shipment.domain.vo.TrackingNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetShipmentHistoryUseCase {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentHistoryRepository historyRepository;

    @Cacheable(
        value = "shipment_histories",
        key = "#trackingNumber + '_' + #limit + '_' + #offset"
    )
    @Transactional(readOnly = true)
    public PaginatedResponse<ShipmentHistoryResponse> execute(String trackingNumber, Integer limit, Integer offset) {
        int finalOffset = (offset == null) ? 0 : Math.max(0, offset);
        int finalLimit = (limit == null) ? 20 : limit;
        finalLimit = Math.max(1, Math.min(100, finalLimit));

        int pageNumber = finalOffset / finalLimit;
        Pageable pageable = PageRequest.of(pageNumber, finalLimit);

        Shipment shipment = shipmentRepository.findByTrackingNumber(new TrackingNumber(trackingNumber))
                .orElseThrow(() -> new ShipmentNotFoundException(trackingNumber));
        
        Page<ShipmentHistory> page = historyRepository.findByShipmentId(shipment.shipmentId(), pageable);

        List<ShipmentHistoryResponse> data = page.getContent().stream()
                .map(ShipmentHistoryResponse::from)
                .toList();

        PaginatedMeta meta = PaginatedMeta.builder()
                .offset(finalOffset)
                .limit(finalLimit)
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        return PaginatedResponse.<ShipmentHistoryResponse>builder()
                .data(data)
                .meta(meta)
                .build();
    }
}
