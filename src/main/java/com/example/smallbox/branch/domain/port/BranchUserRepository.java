package com.example.smallbox.branch.domain.port;

import com.example.smallbox.branch.domain.BranchUser;
import com.example.smallbox.shared.domain.BranchID;
import com.example.smallbox.shared.domain.UserId;

import java.util.List;
import java.util.Optional;

public interface BranchUserRepository {
    BranchUser save(BranchUser branchUser);
    Optional<BranchUser> findById(BranchID branchId, UserId userId);
    List<BranchUser> findByBranchId(BranchID branchId);
    boolean existsById(BranchID branchId, UserId userId);
}
