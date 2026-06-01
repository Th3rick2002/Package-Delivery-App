package com.example.smallbox.shared.infrastructure.web;

import com.example.smallbox.shared.application.GetCitiesService;
import com.example.smallbox.shared.application.dto.CityResponse;
import com.example.smallbox.shared.application.dto.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/cities")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Locations", description = "Endpoints for retrieving location data like cities and departments")
public class CityController {
    private final GetCitiesService getCitiesService;

    @Operation(
            summary = "List all cities",
            description = "Returns a paginated list of all cities supported by the system.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Paginated list of cities retrieved successfully",
                            content = @Content(schema = @Schema(implementation = PaginatedResponse.class))
                    )
            }
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'BRANCH_ADMIN', 'EMPLOYEE', 'CLIENT')")
    public ResponseEntity<PaginatedResponse<CityResponse>> getCities(
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset
    ) {
        return ResponseEntity.ok(getCitiesService.execute(limit, offset));
    }
}
