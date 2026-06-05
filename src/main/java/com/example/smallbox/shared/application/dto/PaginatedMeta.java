package com.example.smallbox.shared.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Metadata for paginated responses")
public class PaginatedMeta {
    @Schema(description = "The offset from which the page starts", example = "0")
    private long offset;

    @Schema(description = "The maximum number of items per page", example = "20")
    private int limit;

    @Schema(description = "The total number of items available", example = "100")
    private long totalElements;

    @Schema(description = "The total number of pages available", example = "5")
    private int totalPages;
}
