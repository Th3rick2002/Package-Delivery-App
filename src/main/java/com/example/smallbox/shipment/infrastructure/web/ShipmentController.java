package com.example.smallbox.shipment.infrastructure.web;

import com.example.smallbox.auth.infrastructure.security.service.CustomUserPrincipal;
import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.shipment.application.CreateShipmentUseCase;
import com.example.smallbox.shipment.application.GetShipmentHistoryUseCase;
import com.example.smallbox.shipment.application.UpdateShipmentStatusUseCase;
import com.example.smallbox.shipment.application.dto.CreateShipmentRequest;
import com.example.smallbox.shipment.application.dto.ShipmentHistoryResponse;
import com.example.smallbox.shipment.application.dto.ShipmentResponse;
import com.example.smallbox.shipment.application.dto.UpdateShipmentStatusRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
@Slf4j
public class ShipmentController {

    private final CreateShipmentUseCase createShipmentUseCase;
    private final UpdateShipmentStatusUseCase updateShipmentStatusUseCase;
    private final GetShipmentHistoryUseCase getShipmentHistoryUseCase;

    @PostMapping
    public ResponseEntity<ShipmentResponse> createShipment(@Valid @RequestBody CreateShipmentRequest request) {
        ShipmentResponse response = createShipmentUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateShipmentStatusRequest request,
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        UserId changedBy = new UserId(principal.getUserId());
        updateShipmentStatusUseCase.execute(id, request.newStatus(), changedBy, request.comments());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<ShipmentHistoryResponse>> getHistory(@PathVariable Long id) {
        List<ShipmentHistoryResponse> response = getShipmentHistoryUseCase.execute(id).stream()
                .map(ShipmentHistoryResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }
}
