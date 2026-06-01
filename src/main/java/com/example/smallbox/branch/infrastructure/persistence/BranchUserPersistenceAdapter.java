package com.example.smallbox.branch.infrastructure.persistence;

import com.example.smallbox.branch.domain.BranchUser;
import com.example.smallbox.branch.domain.port.BranchUserRepository;
import com.example.smallbox.branch.infrastructure.persistence.entities.BranchUserJpaEntity;
import com.example.smallbox.branch.infrastructure.persistence.mapper.BranchUserMapper;
import com.example.smallbox.shared.domain.BranchID;
import com.example.smallbox.shared.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BranchUserPersistenceAdapter implements BranchUserRepository {

    private final JpaBranchUserRepository jpaBranchUserRepository;

    @Override
    public BranchUser save(BranchUser branchUser) {
        BranchUserJpaEntity entity = BranchUserMapper.toJpaEntity(branchUser);
        return BranchUserMapper.toDomain(jpaBranchUserRepository.save(entity));
    }

    @Override
    public Optional<BranchUser> findById(BranchID branchId, UserId userId) {
        return jpaBranchUserRepository.findById(new BranchUserJpaEntity.BranchUserId(branchId.id(), userId.uuid()))
                .map(BranchUserMapper::toDomain);
    }

    @Override
    public Page<BranchUser> findByBranchId(BranchID branchId, Pageable pageable) {
        return jpaBranchUserRepository.findByBranchId(branchId.id(), pageable)
                .map(BranchUserMapper::toDomain);
    }

    @Override
    public boolean existsById(BranchID branchId, UserId userId) {
        return jpaBranchUserRepository.existsById(new BranchUserJpaEntity.BranchUserId(branchId.id(), userId.uuid()));
    }
}
