package com.example.smallbox.shared.infrastructure.persistence;

import com.example.smallbox.shared.domain.LocationId;
import com.example.smallbox.shared.domain.LocationInfo;
import com.example.smallbox.shared.domain.port.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LocationPersistenceAdapter implements LocationRepository {

    private final JpaDepartmentRepository jpaDepartmentRepository;

    @Override
    public boolean exists(LocationId locationId) {
        return this.jpaDepartmentRepository.existsById(locationId.cityId());
    }

    @Override
    public Optional<LocationInfo> findById(LocationId locationId) {
        return this.jpaDepartmentRepository.findById(locationId.cityId())
                .map(departmentJpaEntity -> new LocationInfo(
                        departmentJpaEntity.getDeparmentName(),
                        departmentJpaEntity.getCountry().getCountryName(),
                        departmentJpaEntity.getDeliveryZoneCode()
                ));
    }
}
