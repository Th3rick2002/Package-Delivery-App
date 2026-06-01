package com.example.smallbox.branch.infrastructure.web;

import com.example.smallbox.branch.application.BranchUserService;
import com.example.smallbox.branch.application.dto.AssignUserRequest;
import com.example.smallbox.branch.application.dto.BranchUserResponse;
import com.example.smallbox.branch.application.dto.UpdateBranchUserStatusRequest;
import com.example.smallbox.shared.application.dto.ApiErrorResponse;
import com.example.smallbox.shared.application.dto.PaginatedResponse;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/branches/{branchId}/users")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Branch Users", description = "Endpoints for managing user assignments to branches")
public class BranchUserController {

    private final BranchUserService branchUserService;

    @Operation(
            summary = "Assign a user to a branch",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User assigned successfully",
                            content = @Content(schema = @Schema(implementation = BranchUserResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Branch or User not found",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "User already assigned to this branch",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'BRANCH_ADMIN')")
    public ResponseEntity<BranchUserResponse> assignUser(
            @PathVariable Integer branchId,
            @Valid @RequestBody AssignUserRequest request) {
        return ResponseEntity.ok(branchUserService.assignUser(branchId, request));
    }

    @Operation(
            summary = "Update user status in a branch",
            description = "Enables or disables a user's access to a specific branch.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Status updated",
                            content = @Content(schema = @Schema(implementation = BranchUserResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Assignment not found",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @PatchMapping("/{userId}/status")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'BRANCH_ADMIN')")
    public ResponseEntity<BranchUserResponse> updateStatus(
            @PathVariable Integer branchId,
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateBranchUserStatusRequest request) {
        return ResponseEntity.ok(branchUserService.updateStatus(branchId, userId, request));
    }

    @Operation(
            summary = "List users in a branch",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Paginated list of users assigned to the branch",
                            content = @Content(schema = @Schema(implementation = PaginatedResponse.class))
                    )
            }
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'BRANCH_ADMIN')")
    public ResponseEntity<PaginatedResponse<BranchUserResponse>> listUsersByBranch(
            @PathVariable Integer branchId,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset
    ) {
        return ResponseEntity.ok(branchUserService.listUsersByBranch(branchId, limit, offset));
    }
}
