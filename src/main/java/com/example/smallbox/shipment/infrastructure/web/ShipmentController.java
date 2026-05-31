package com.example.smallbox.shipment.infrastructure.web;

import com.example.smallbox.shared.application.dto.ApiErrorResponse;
import com.example.smallbox.shipment.application.CreateShipmentUseCase;
import com.example.smallbox.shipment.application.dto.CreateShipmentRequest;
import com.example.smallbox.shipment.application.dto.ShipmentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Shipments", description = "Endpoints for creating and managing shipments")
public class ShipmentController {

    private final CreateShipmentUseCase createShipmentUseCase;

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
}
