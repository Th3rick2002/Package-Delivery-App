package com.example.smallbox.branch.infrastructure.persistence;

import com.example.smallbox.branch.domain.Branch;
import com.example.smallbox.branch.domain.port.BranchRepository;
import com.example.smallbox.branch.infrastructure.persistence.entities.BranchEntity;
import com.example.smallbox.branch.infrastructure.persistence.mapper.BranchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BranchPersistenceAdapter implements BranchRepository {

    private final JpaBranchRepository jpaBranchRepository;

    @Override
    public Branch save(Branch branch) {
        BranchEntity entity = BranchMapper.toJpaEntity(branch);
        return BranchMapper.toDomain(jpaBranchRepository.save(entity));
    }

    @Override
    public Optional<Branch> findById(Integer id) {
        return jpaBranchRepository.findById(id)
                .map(BranchMapper::toDomain);
    }

    @Override
    public Optional<Branch> findByLocationId(Integer locationId) {
        return jpaBranchRepository.findByDepartment_IdDepartment(locationId).map(BranchMapper::toDomain);
    }

    @Override
    public List<Branch> findAll() {
        return jpaBranchRepository.findAll().stream()
                .map(BranchMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(Integer id) {
        return jpaBranchRepository.existsById(id);
    }

    @Override
    public void update(Branch branch) {
        jpaBranchRepository.save(BranchMapper.toJpaEntity(branch));
    }

    @Override
    public void deleteById(Integer id) {
        jpaBranchRepository.deleteById(id);
    }
}
