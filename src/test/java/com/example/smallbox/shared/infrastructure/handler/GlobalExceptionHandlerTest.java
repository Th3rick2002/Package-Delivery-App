package com.example.smallbox.shared.infrastructure.handler;

import com.example.smallbox.shared.application.dto.ApiErrorResponse;
import com.example.smallbox.shipment.domain.exception.ShipmentValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    @Test
    void handleDomainExceptionPreservesSemanticCode() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ShipmentValidationException exception = new ShipmentValidationException(
                "UNSUPPORTED_WEIGHT_UNIT",
                "Unsupported weight unit. Only KG is supported"
        );

        ResponseEntity<ApiErrorResponse> response = handler.handleDomainException(exception);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("UNSUPPORTED_WEIGHT_UNIT", response.getBody().code());
        assertEquals("Unsupported weight unit. Only KG is supported", response.getBody().message());
        assertEquals(400, response.getBody().status());
    }
}
