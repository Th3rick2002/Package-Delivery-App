package com.example.smallbox.branch.domain;

import com.example.smallbox.shared.domain.BranchID;
import com.example.smallbox.shared.domain.UserId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class BranchUser {
    private final BranchID branchId;
    private final UserId userId;
    private boolean active;

    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static BranchUser create(BranchID branchId, UserId userId) {
        return BranchUser.builder()
                .branchId(branchId)
                .userId(userId)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void updateStatus(boolean active) {
        this.active = active;
        this.updatedAt = LocalDateTime.now();
    }
}
