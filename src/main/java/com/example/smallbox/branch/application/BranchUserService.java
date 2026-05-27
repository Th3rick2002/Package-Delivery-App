package com.example.smallbox.branch.application;

import com.example.smallbox.branch.application.dto.AssignUserRequest;
import com.example.smallbox.branch.application.dto.BranchUserResponse;
import com.example.smallbox.branch.application.dto.UpdateBranchUserStatusRequest;
import com.example.smallbox.branch.domain.Branch;
import com.example.smallbox.branch.domain.BranchUser;
import com.example.smallbox.branch.domain.exception.*;
import com.example.smallbox.branch.domain.port.BranchRepository;
import com.example.smallbox.branch.domain.port.BranchUserRepository;
import com.example.smallbox.shared.domain.BranchID;
import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.user.domain.User;
import com.example.smallbox.user.domain.UserRepository;
import com.example.smallbox.user.domain.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchUserService {

    private final BranchUserRepository branchUserRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;

    @Transactional
    public BranchUserResponse assignUser(Integer branchId, AssignUserRequest request) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new BranchNotFoundException(branchId));

        if (branch.getDeletedAt() != null) {
            throw new BranchInactiveException(branchId);
        }

        UserId userId = new UserId(request.userId());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(request.userId().toString()));

        validateRole(user.getRole().getName());

        if (branchUserRepository.existsById(new BranchID(branchId), userId)) {
            throw new BranchUserAlreadyExistsException(branchId, request.userId());
        }

        BranchUser branchUser = BranchUser.create(new BranchID(branchId), userId);
        return mapToResponse(branchUserRepository.save(branchUser));
    }

    @Transactional
    public BranchUserResponse updateStatus(Integer branchId, UUID userId, UpdateBranchUserStatusRequest request) {
        BranchUser branchUser = branchUserRepository.findById(new BranchID(branchId), new UserId(userId))
                .orElseThrow(() -> new BranchUserNotFoundException(branchId, userId));

        branchUser.updateStatus(request.active());
        return mapToResponse(branchUserRepository.save(branchUser));
    }

    public List<BranchUserResponse> listUsersByBranch(Integer branchId) {
        if (!branchRepository.existsById(branchId)) {
            throw new BranchNotFoundException(branchId);
        }

        return branchUserRepository.findByBranchId(new BranchID(branchId)).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private void validateRole(String roleName) {
        if ("SUPER_ADMIN".equals(roleName) || "CLIENT".equals(roleName)) {
            throw new InvalidRoleForBranchException(roleName);
        }
        
        if (!"BRANCH_ADMIN".equals(roleName) && !"EMPLOYEE".equals(roleName)) {
            throw new InvalidRoleForBranchException(roleName);
        }
    }

    private BranchUserResponse mapToResponse(BranchUser domain) {
        return new BranchUserResponse(
                domain.getBranchId().id(),
                domain.getUserId().uuid(),
                domain.isActive(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }
}
