package com.example.smallbox.shipment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.UUID;

@Schema(description = "Request to create a new shipment")
public record CreateShipmentRequest(
        @Schema(description = "ID of the user sending the shipment")
        @NotNull UUID senderId,

        @Schema(description = "Recipient details")
        @NotNull @Valid RecipientRequest recipient,

        @Schema(description = "ID of the branch where the shipment originates")
        @NotNull Integer originBranchId,

        @Schema(description = "ID of the branch where the shipment will be delivered")
        @NotNull Integer destinationBranchId,

        @Schema(description = "ID of the destination city")
        @NotNull Integer destinationCityId,

        @Schema(description = "Exact delivery address", example = "Calle 123 #45-67, Edificio Torres, Apto 402")
        @NotBlank String exactAddress,

        @Schema(description = "List of packages included in the shipment")
        @NotEmpty @Valid List<PackageRequest> packages
) {
    @Schema(description = "Recipient information for the shipment")
    public record RecipientRequest(
            @Schema(description = "Recipient's first name", example = "Jane")
            @NotBlank String firstName,

            @Schema(description = "Recipient's second name (optional)", example = "Marie")
            String secondName,

            @Schema(description = "Recipient's last name", example = "Doe")
            @NotBlank String lastName,

            @Schema(description = "Recipient's second last name (optional)", example = "Smith")
            String secondLastName,

            @Schema(description = "Recipient's contact phone", example = "+573009876543")
            @NotBlank String phone,

            @Schema(description = "Recipient's email", example = "jane.doe@example.com")
            @NotBlank String email
    ) {}

    @Schema(description = "Package details")
    public record PackageRequest(
            @Schema(description = "Description of the package contents", example = "Laptop and accessories")
            @NotBlank String description,

            @Schema(description = "Weight of the package", example = "2.5")
            @Positive double weight,

            @Schema(description = "Unit of weight (KG, LB)", example = "KG")
            @NotBlank String weightUnit,

            @Schema(description = "Length of the package", example = "40.0")
            @Positive double length,

            @Schema(description = "Width of the package", example = "30.0")
            @Positive double width,

            @Schema(description = "Height of the package", example = "10.0")
            @Positive double height,

            @Schema(description = "Unit of dimensions (CM, IN)", example = "CM")
            @NotBlank String dimensionsUnit
    ) {}
}
