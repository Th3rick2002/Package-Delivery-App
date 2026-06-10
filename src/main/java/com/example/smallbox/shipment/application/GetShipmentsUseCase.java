package com.example.smallbox.shipment.application;

import com.example.smallbox.shared.application.dto.PaginatedMeta;
import com.example.smallbox.shared.application.dto.PaginatedResponse;
import com.example.smallbox.shipment.application.dto.ShipmentSummaryResponse;
import com.example.smallbox.shipment.domain.Shipment;
import com.example.smallbox.shipment.domain.port.ShipmentRepository;
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
public class GetShipmentsUseCase {

    private final ShipmentRepository shipmentRepository;

    @Cacheable(
        value = "shipments",
        key = "'all_' + #limit + '_' + #offset"
    )
    @Transactional(readOnly = true)
    public PaginatedResponse<ShipmentSummaryResponse> execute(Integer limit, Integer offset) {
        int finalOffset = (offset == null) ? 0 : Math.max(0, offset);
        int finalLimit = (limit == null) ? 20 : limit;
        finalLimit = Math.max(1, Math.min(100, finalLimit));

        int pageNumber = finalOffset / finalLimit;
        Pageable pageable = PageRequest.of(pageNumber, finalLimit);

        Page<Shipment> page = shipmentRepository.findAll(pageable);

        List<ShipmentSummaryResponse> data = page.getContent().stream()
                .map(ShipmentSummaryResponse::from)
                .toList();

        PaginatedMeta meta = PaginatedMeta.builder()
                .offset(finalOffset)
                .limit(finalLimit)
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        return PaginatedResponse.<ShipmentSummaryResponse>builder()
                .data(data)
                .meta(meta)
                .build();
    }
}
