package com.example.smallbox.shipment.infrastructure.web;

import com.example.smallbox.auth.infrastructure.security.service.CustomUserPrincipal;
import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.shipment.application.CreateShipmentUseCase;
import com.example.smallbox.shipment.application.GetShipmentHistoryUseCase;
import com.example.smallbox.shipment.application.GetShipmentUseCase;
import com.example.smallbox.shipment.application.UpdateShipmentStatusUseCase;
import com.example.smallbox.shipment.application.dto.ShipmentResponse;
import com.example.smallbox.shipment.application.dto.UpdateShipmentStatusRequest;
import com.example.smallbox.shipment.domain.enums.ShipmentStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShipmentController.class)
class ShipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateShipmentUseCase createShipmentUseCase;

    @MockitoBean
    private UpdateShipmentStatusUseCase updateShipmentStatusUseCase;

    @MockitoBean
    private GetShipmentHistoryUseCase getShipmentHistoryUseCase;

    @MockitoBean
    private GetShipmentUseCase getShipmentUseCase;

    private String trackingNumber = "SB-20260531-143000123456";
    private CustomUserPrincipal principal;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.randomUUID();
        principal = new CustomUserPrincipal(userId, "test@example.com", "password", List.of());
    }

    @Test
    @WithMockUser
    void getShipment_ShouldReturnShipmentDetails() throws Exception {
        ShipmentResponse response = new ShipmentResponse(
                trackingNumber,
                "CREATED",
                new BigDecimal("25.50"),
                "USD",
                UUID.randomUUID(),
                new ShipmentResponse.RecipientInfo("Jane Doe", "3009876543", "jane.doe@example.com"),
                1,
                "Address",
                1,
                2,
                List.of(),
                LocalDateTime.now()
        );

        when(getShipmentUseCase.execute(trackingNumber)).thenReturn(response);

        mockMvc.perform(get("/api/v1/shipments/{trackingNumber}", trackingNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trackingNumber").value(trackingNumber))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    void updateStatus_ShouldCallUseCaseWithTrackingNumber() throws Exception {
        UpdateShipmentStatusRequest request = new UpdateShipmentStatusRequest(ShipmentStatus.RECEIVED_ORIGIN, "Received");

        mockMvc.perform(patch("/api/v1/shipments/{trackingNumber}/status", trackingNumber)
                        .with(csrf())
                        .with(user(principal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(updateShipmentStatusUseCase).execute(
                eq(trackingNumber),
                eq(ShipmentStatus.RECEIVED_ORIGIN),
                any(UserId.class),
                eq("Received")
        );
    }

    @Test
    @WithMockUser
    void getHistory_ShouldReturnHistoryList() throws Exception {
        when(getShipmentHistoryUseCase.execute(eq(trackingNumber), any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/shipments/{trackingNumber}/history", trackingNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.meta").exists());
    }
}
