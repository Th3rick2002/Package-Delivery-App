package com.example.smallbox.branch.application;

import com.example.smallbox.auth.infrastructure.security.service.StaffUserPrincipal;
import com.example.smallbox.branch.application.dto.AssignUserRequest;
import com.example.smallbox.branch.application.dto.BranchUserResponse;
import com.example.smallbox.branch.application.dto.UpdateBranchUserStatusRequest;
import com.example.smallbox.branch.domain.Branch;
import com.example.smallbox.branch.domain.BranchUser;
import com.example.smallbox.branch.domain.exception.*;
import com.example.smallbox.branch.domain.port.BranchRepository;
import com.example.smallbox.branch.domain.port.BranchUserRepository;
import com.example.smallbox.shared.application.dto.PaginatedMeta;
import com.example.smallbox.shared.application.dto.PaginatedResponse;
import com.example.smallbox.shared.domain.BranchID;
import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.user.domain.User;
import com.example.smallbox.user.domain.UserRepository;
import com.example.smallbox.user.domain.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @CacheEvict(value =  {"branchUsers", "branchUserGlobal"}, allEntries = true)
    @Transactional
    public BranchUserResponse assignUser(AssignUserRequest request) {
        Branch branch = branchRepository.findById(request.branchId())
                .orElseThrow(() -> new BranchNotFoundException(request.branchId()));

        if (branch.getDeletedAt() != null) {
            throw new BranchInactiveException(request.branchId());
        }

        UserId userId = new UserId(request.userId());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(request.userId().toString()));

        validateRole(user.getRole().getName());

        if (branchUserRepository.existsById(new BranchID(branch.getId().id()), user.getId())) {
            throw new BranchUserAlreadyExistsException(branch.getId().id(), user.getId().uuid());
        }

        BranchUser branchUser = BranchUser.create(new BranchID(branch.getId().id()), user.getId());
        return mapToResponse(branchUserRepository.save(branchUser));
    }

    @CacheEvict(value = {"branchUsers", "branchUserGlobal"}, allEntries = true)
    @Transactional
    public BranchUserResponse updateStatus(UpdateBranchUserStatusRequest request) {
        BranchUser branchUser = branchUserRepository.findById(new BranchID(request.branchId()), new UserId(request.userId()))
                .orElseThrow(() -> new BranchUserNotFoundException(request.branchId(), request.userId()));

        branchUser.updateStatus(request.active());
        return mapToResponse(branchUserRepository.save(branchUser));
    }

    @Cacheable(
            value = "branchUserGlobal",
            key = "(#branchId != null ? #branchId : 'all') + '_' +" +
                    " (#offset != null ? #offset : 0) + '_' +" +
                    " (#limit != null ? #limit : 20)"
    )
    public PaginatedResponse<BranchUserResponse> listUserBranch(Integer branchId, Integer limit, Integer offset) {
        int finalOffset = (offset == null) ? 0 : Math.max(0, offset);
        int finalLimit = (limit == null) ? 20 : limit;
        finalLimit = Math.max(1, Math.min(100, finalLimit));

        int pageNumber = finalOffset / finalLimit;
        Pageable pageable = PageRequest.of(pageNumber, finalLimit);

        Page<BranchUser> page;

        if (branchId != null) {
            if (!branchRepository.existsById(branchId)) {
                throw new BranchNotFoundException(branchId);
            }
            page = branchUserRepository.findByBranchId(new BranchID(branchId), pageable);
        } else {
            page = branchUserRepository.findAll(pageable);
        }

        return getBranchUserResponsePaginatedResponse(finalOffset, finalLimit, page);
    }

    @Cacheable(
            value = "branchUsers",
            key = "#principal.branchId + '_' +" +
                    " (#offset != null ? #offset : 0) + '_' + " +
                    "(#limit != null ? #limit : 20)"
    )
    public PaginatedResponse<BranchUserResponse> listUsersByBranch(Integer limit, Integer offset, StaffUserPrincipal principal) {
        Integer branchId = principal.getBranchId();

        if (branchId == null)
            throw new BranchUserNotAssignedException(principal.getUserId());

        if (!branchRepository.existsById(branchId))
            throw new BranchNotFoundException(branchId);

        int finalOffset = (offset == null) ? 0 : Math.max(0, offset);
        int finalLimit = (limit == null) ? 20 : limit;
        finalLimit = Math.max(1, Math.min(100, finalLimit));

        int pageNumber = finalOffset / finalLimit;
        Pageable pageable = PageRequest.of(pageNumber, finalLimit);

        Page<BranchUser> page = branchUserRepository.findByBranchId(new BranchID(branchId), pageable);

        return getBranchUserResponsePaginatedResponse(finalOffset, finalLimit, page);
    }


    private PaginatedResponse<BranchUserResponse> getBranchUserResponsePaginatedResponse(int finalOffset, int finalLimit, Page<BranchUser> page) {
        List<BranchUserResponse> data = page.getContent().stream()
                .map(this::mapToResponse)
                .toList();

        PaginatedMeta meta = PaginatedMeta.builder()
                .offset(finalOffset)
                .limit(finalLimit)
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        return PaginatedResponse.<BranchUserResponse>builder()
                .data(data)
                .meta(meta)
                .build();
    }

    private void validateRole(String roleName) {
        boolean isAllowed = "ROLE_EMPLOYEE".equals(roleName) || "ROLE_BRANCH_ADMIN".equals(roleName);

        if (!isAllowed) {
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
