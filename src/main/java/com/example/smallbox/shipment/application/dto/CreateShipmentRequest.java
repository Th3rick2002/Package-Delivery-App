package com.example.smallbox.shipment.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.UUID;

public record CreateShipmentRequest(
        @NotNull UUID senderId,
        @NotNull @Valid RecipientRequest recipient,
        @NotNull Integer originBranchId,
        @NotNull Integer destinationBranchId,
        @NotNull Integer destinationCityId,
        @NotBlank String exactAddress,
        @NotEmpty @Valid List<PackageRequest> packages
) {
    public record RecipientRequest(
            @NotBlank String firstName,
            String secondName,
            @NotBlank String lastName,
            String secondLastName,
            @NotBlank String phone,
            @NotBlank String email
    ) {}

    public record PackageRequest(
            @NotBlank String description,
            @Positive double weight,
            @NotBlank String weightUnit,
            @Positive double length,
            @Positive double width,
            @Positive double height,
            @NotBlank String dimensionsUnit
    ) {}
}
