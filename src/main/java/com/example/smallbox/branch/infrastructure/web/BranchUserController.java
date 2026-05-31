package com.example.smallbox.branch.infrastructure.web;

import com.example.smallbox.branch.application.BranchUserService;
import com.example.smallbox.branch.application.dto.AssignUserRequest;
import com.example.smallbox.branch.application.dto.BranchUserResponse;
import com.example.smallbox.branch.application.dto.UpdateBranchUserStatusRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/branches/{branchId}/users")
@Slf4j
@RequiredArgsConstructor
public class BranchUserController {

    private final BranchUserService branchUserService;

    @PostMapping
    public ResponseEntity<BranchUserResponse> assignUser(
            @PathVariable Integer branchId,
            @Valid @RequestBody AssignUserRequest request) {
        return ResponseEntity.ok(branchUserService.assignUser(branchId, request));
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<BranchUserResponse> updateStatus(
            @PathVariable Integer branchId,
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateBranchUserStatusRequest request) {
        return ResponseEntity.ok(branchUserService.updateStatus(branchId, userId, request));
    }

    @GetMapping
    public ResponseEntity<List<BranchUserResponse>> listUsersByBranch(@PathVariable Integer branchId) {
        return ResponseEntity.ok(branchUserService.listUsersByBranch(branchId));
    }
}
