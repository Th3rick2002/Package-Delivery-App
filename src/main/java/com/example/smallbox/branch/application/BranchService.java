package com.example.smallbox.branch.application;

import com.example.smallbox.branch.application.dto.BranchResponse;
import com.example.smallbox.branch.domain.Branch;
import com.example.smallbox.branch.domain.port.BranchRepository;
import com.example.smallbox.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;

    @Cacheable(value = "branches", key = "'all'")
    public List<BranchResponse> findAll() {
        return branchRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private BranchResponse mapToResponse(Branch entity) {
        return new BranchResponse(
                entity.getId().id(),
                entity.getName(),
                entity.getCity().cityId(),
                entity.getPhone().value(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
