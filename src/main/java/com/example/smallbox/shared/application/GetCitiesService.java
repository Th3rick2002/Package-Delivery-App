package com.example.smallbox.shared.application;

import com.example.smallbox.shared.application.dto.CityResponse;
import com.example.smallbox.shared.infrastructure.persistence.JpaDepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCitiesService {
    private final JpaDepartmentRepository jpaDepartmentRepository;

    @Transactional(readOnly = true)
    public List<CityResponse> execute() {
        return jpaDepartmentRepository.findAll().stream()
                .map(dept -> new CityResponse(
                        dept.getIdDepartment(),
                        dept.getDepartmentName(),
                        dept.getCountry().getCountryName()
                ))
                .toList();
    }
}
