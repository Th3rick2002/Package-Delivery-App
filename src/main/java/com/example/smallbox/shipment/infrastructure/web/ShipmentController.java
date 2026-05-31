package com.example.smallbox.shipment.infrastructure.web;

import com.example.smallbox.shared.application.dto.ApiErrorResponse;
import com.example.smallbox.auth.infrastructure.security.service.CustomUserPrincipal;
import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.shipment.application.CreateShipmentUseCase;
import com.example.smallbox.shipment.application.GetShipmentHistoryUseCase;
import com.example.smallbox.shipment.application.UpdateShipmentStatusUseCase;
import com.example.smallbox.shipment.application.dto.CreateShipmentRequest;
import com.example.smallbox.shipment.application.dto.ShipmentHistoryResponse;
import com.example.smallbox.shipment.application.dto.ShipmentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Shipments", description = "Endpoints for creating and managing shipments")
public class ShipmentController {

    private final CreateShipmentUseCase createShipmentUseCase;
    private final UpdateShipmentStatusUseCase updateShipmentStatusUseCase;
    private final GetShipmentHistoryUseCase getShipmentHistoryUseCase;

    @Operation(
            summary = "Create a new shipment",
            description = "Creates a shipment with one or more packages, calculates price, and assigns a tracking number.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Shipment created successfully",
                            content = @Content(schema = @Schema(implementation = ShipmentResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
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
