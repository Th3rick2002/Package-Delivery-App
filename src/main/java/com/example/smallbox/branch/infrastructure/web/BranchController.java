package com.example.smallbox.branch.infrastructure.web;

import com.example.smallbox.branch.application.BranchService;
import com.example.smallbox.branch.application.dto.BranchResponse;
import com.example.smallbox.branch.application.dto.CreateBranchRequest;
import com.example.smallbox.branch.application.dto.UpdateBranchRequest;
import com.example.smallbox.shared.application.dto.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/branches")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Branches", description = "Endpoints for branch management")
public class BranchController {
    private final BranchService branchService;

    @Operation(
            summary = "List all branches",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of all branches",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BranchResponse.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<BranchResponse>> getAll() {
        return ResponseEntity.ok(branchService.findAll());
    }

    @Operation(
            summary = "Get branch by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Branch found",
                            content = @Content(schema = @Schema(implementation = BranchResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Branch not found",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<BranchResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(branchService.getById(id));
    }

    @Operation(
            summary = "Get branch by Location ID",
            description = "Retrieves the branch associated with a specific location (department).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Branch found",
                            content = @Content(schema = @Schema(implementation = BranchResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Branch not found for this location",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @GetMapping("/location/{locationId}")
    public ResponseEntity<BranchResponse> getByLocationId(@PathVariable Integer locationId) {
        return ResponseEntity.ok(branchService.getByLocationId(locationId));
    }

    @Operation(
            summary = "Create a new branch",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Branch created",
                            content = @Content(schema = @Schema(implementation = BranchResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<BranchResponse> create(@Valid @RequestBody CreateBranchRequest request) {
        return ResponseEntity.ok(branchService.create(request));
    }

    @Operation(
            summary = "Update a branch",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Branch updated",
                            content = @Content(schema = @Schema(implementation = BranchResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Branch not found",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<BranchResponse> update(@PathVariable Integer id, @Valid @RequestBody UpdateBranchRequest request) {
        return ResponseEntity.ok(branchService.update(id, request));
    }

    @Operation(
            summary = "Delete a branch",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Branch deleted"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Branch not found",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        branchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
