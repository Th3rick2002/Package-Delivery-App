package com.example.smallbox.branch.infrastructure.web;

import com.example.smallbox.auth.infrastructure.security.service.StaffUserPrincipal;
import com.example.smallbox.branch.application.BranchUserService;
import com.example.smallbox.branch.application.dto.AssignUserRequest;
import com.example.smallbox.branch.application.dto.BranchUserResponse;
import com.example.smallbox.branch.application.dto.UpdateBranchUserStatusRequest;
import com.example.smallbox.shared.application.dto.ApiErrorResponse;
import com.example.smallbox.shared.application.dto.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/branch-users")
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
            @Valid @RequestBody AssignUserRequest request
    ) {
        return ResponseEntity.ok(branchUserService.assignUser(request));
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
    @PatchMapping("/status")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'BRANCH_ADMIN')")
    public ResponseEntity<BranchUserResponse> updateStatus(
            @Valid @RequestBody UpdateBranchUserStatusRequest request
    ) {
        return ResponseEntity.ok(branchUserService.updateStatus(request));
    }

    @Operation(
            summary = "List users in all branches",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Paginated list of users assigned to the branch",
                            content = @Content(schema = @Schema(implementation = PaginatedResponse.class))
                    )
            }
    )
    @GetMapping("/global")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<PaginatedResponse<BranchUserResponse>> listUsersBranch(
            @RequestParam(value = "branch", required = false) Integer branchId,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset
    ) {
        return ResponseEntity.ok(branchUserService.listUserBranch(branchId, limit, offset));
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
    @GetMapping("/my-branch")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'BRANCH_ADMIN')")
    public ResponseEntity<PaginatedResponse<BranchUserResponse>> listUsersByBranch(
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @AuthenticationPrincipal StaffUserPrincipal principal
    ) {
        return ResponseEntity.ok(branchUserService.listUsersByBranch(limit, offset, principal));
    }
}
