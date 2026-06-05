package com.example.smallbox.branch.domain.port;

import com.example.smallbox.branch.domain.BranchUser;
import com.example.smallbox.shared.domain.BranchID;
import com.example.smallbox.shared.domain.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BranchUserRepository {
    BranchUser save(BranchUser branchUser);
    Page<BranchUser> findAll(Pageable pageable);
    Optional<BranchUser> findById(BranchID branchId, UserId userId);
    Optional<BranchUser> findByUserId(UserId userId);
    Page<BranchUser> findByBranchId(BranchID branchId, Pageable pageable);
    boolean existsById(BranchID branchId, UserId userId);
}
