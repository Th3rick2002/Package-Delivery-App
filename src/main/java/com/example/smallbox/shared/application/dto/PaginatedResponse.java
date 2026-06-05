package com.example.smallbox.shared.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Wrapper for paginated responses")
public class PaginatedResponse<T> {
    @Schema(description = "The list of items for the current page")
    private List<T> data;

    @Schema(description = "Pagination metadata")
    private PaginatedMeta meta;
}
