package com.example.smallbox.branch.application;

import com.example.smallbox.branch.application.dto.BranchResponse;
import com.example.smallbox.branch.application.dto.CreateBranchRequest;
import com.example.smallbox.branch.application.dto.UpdateBranchRequest;
import com.example.smallbox.branch.domain.Branch;
import com.example.smallbox.branch.domain.exception.BranchNotFoundException;
import com.example.smallbox.branch.domain.port.BranchRepository;
import com.example.smallbox.shared.domain.LocationId;
import com.example.smallbox.shared.domain.LocationInfo;
import com.example.smallbox.shared.domain.Phone;
import com.example.smallbox.shared.domain.port.LocationRepository;
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
    private final LocationRepository locationRepository;

    @Cacheable(value = "branches", key = "'all'")
    public List<BranchResponse> findAll() {
        return branchRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public BranchResponse create(CreateBranchRequest request) {
        LocationId locationId = new LocationId(request.departmentId());
        LocationInfo locationExists = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("La dirección ingresada no es valida"));

        Phone phone = new Phone(request.phone());
        Branch branch = Branch.create(request.name(), locationId, phone);

        var response = branchRepository.save(branch);

        return mapToResponse(response);
    }

    public BranchResponse update(Integer id, UpdateBranchRequest request) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException(id));

        if (request.name() != null) branch.updateName(request.name());

        if (request.departmentId() != null) {
            LocationId locationId = new LocationId(request.departmentId());
            LocationInfo locationInfo = locationRepository.findById(locationId)
                    .orElseThrow(() -> new IllegalArgumentException("La dirección ingresada no es valida"));
            branch.updateDepartment(locationId);
        }

        if (request.phone() != null) branch.updatePhone(new Phone(request.phone()));

        var response = branchRepository.save(branch);

        return mapToResponse(response);
    }

    public void delete(Integer id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException(id));
        branch.delete();
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
