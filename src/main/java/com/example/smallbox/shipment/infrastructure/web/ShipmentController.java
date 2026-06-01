package com.example.smallbox.shipment.infrastructure.web;

import com.example.smallbox.auth.infrastructure.security.service.CustomUserPrincipal;
import com.example.smallbox.shared.application.dto.ApiErrorResponse;
import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.shipment.application.CreateShipmentUseCase;
import com.example.smallbox.shipment.application.GetShipmentHistoryUseCase;
import com.example.smallbox.shipment.application.GetShipmentUseCase;
import com.example.smallbox.shipment.application.UpdateShipmentStatusUseCase;
import com.example.smallbox.shipment.application.dto.CreateShipmentRequest;
import com.example.smallbox.shipment.application.dto.ShipmentHistoryResponse;
import com.example.smallbox.shipment.application.dto.ShipmentResponse;
import com.example.smallbox.shipment.application.dto.UpdateShipmentStatusRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final GetShipmentUseCase getShipmentUseCase;

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
    @PreAuthorize("hasAnyRole('BRANCH_ADMIN', 'EMPLOYEE', 'CLIENT')")
    public ResponseEntity<ShipmentResponse> createShipment(@Valid @RequestBody CreateShipmentRequest request) {
        ShipmentResponse response = createShipmentUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get shipment details",
            description = "Retrieves the current state and details of a shipment by its tracking number.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Shipment found",
                            content = @Content(schema = @Schema(implementation = ShipmentResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Shipment not found",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @GetMapping("/{trackingNumber}")
    @PreAuthorize("hasAnyRole('BRANCH_ADMIN', 'EMPLOYEE', 'CLIENT')")
    @Cacheable(value = "shipments", key = "#trackingNumber")
    public ResponseEntity<ShipmentResponse> getShipment(
            @Parameter(description = "The tracking number of the shipment", example = "SB-20260531-143000123456")
            @PathVariable String trackingNumber
    ) {
        return ResponseEntity.ok(getShipmentUseCase.execute(trackingNumber));
    }

    @Operation(
            summary = "Update shipment status",
            description = "Updates the status of a shipment. Transitions must follow the defined state machine rules.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Status updated successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid transition or status",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Shipment not found",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @PatchMapping("/{trackingNumber}/status")
    @PreAuthorize("hasAnyRole('BRANCH_ADMIN', 'EMPLOYEE', 'CLIENT')")
    @Caching(evict = {
            @CacheEvict(value = "shipments", key = "#trackingNumber"),
            @CacheEvict(value = "shipment_histories", key = "#trackingNumber")
    })
    public ResponseEntity<Void> updateStatus(
            @Parameter(description = "The tracking number of the shipment", example = "SB-20260531-143000123456")
            @PathVariable String trackingNumber,
            @Valid @RequestBody UpdateShipmentStatusRequest request,
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        UserId changedBy = new UserId(principal.getUserId());
        updateShipmentStatusUseCase.execute(trackingNumber, request.newStatus(), changedBy, request.comments());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get shipment history",
            description = "Retrieves the full history of status changes for a shipment.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "History retrieved successfully",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ShipmentHistoryResponse.class)))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Shipment not found",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @GetMapping("/{trackingNumber}/history")
    @PreAuthorize("hasAnyRole('BRANCH_ADMIN', 'EMPLOYEE', 'CLIENT')")
    @Cacheable(value = "shipment_histories", key = "#trackingNumber")
    public ResponseEntity<List<ShipmentHistoryResponse>> getHistory(
            @Parameter(description = "The tracking number of the shipment", example = "SB-20260531-143000123456")
            @PathVariable String trackingNumber
    ) {
        List<ShipmentHistoryResponse> response = getShipmentHistoryUseCase.execute(trackingNumber).stream()
                .map(ShipmentHistoryResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }
}
