package com.example.smallbox.shared.application;

import com.example.smallbox.shared.application.dto.CityResponse;
import com.example.smallbox.shared.application.dto.PaginatedMeta;
import com.example.smallbox.shared.application.dto.PaginatedResponse;
import com.example.smallbox.shared.infrastructure.persistence.JpaDepartmentRepository;
import com.example.smallbox.shared.infrastructure.persistence.entities.DepartmentJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCitiesService {
    private final JpaDepartmentRepository jpaDepartmentRepository;

    @Cacheable(value = "cities", key = "'all' + #limit + #offset")
    @Transactional(readOnly = true)
    public PaginatedResponse<CityResponse> execute(Integer limit, Integer offset) {
        int finalOffset = (offset == null) ? 0 : Math.max(0, offset);
        int finalLimit = (limit == null) ? 20 : limit;
        finalLimit = Math.max(1, Math.min(100, finalLimit));

        int pageNumber = finalOffset / finalLimit;
        Pageable pageable = PageRequest.of(pageNumber, finalLimit);

        Page<DepartmentJpaEntity> page = jpaDepartmentRepository.findAll(pageable);

        List<CityResponse> data = page.getContent().stream()
                .map(dept -> new CityResponse(
                        dept.getIdDepartment(),
                        dept.getDepartmentName(),
                        dept.getCountry().getCountryName()
                ))
                .toList();

        PaginatedMeta meta = PaginatedMeta.builder()
                .offset(finalOffset)
                .limit(finalLimit)
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        return PaginatedResponse.<CityResponse>builder()
                .data(data)
                .meta(meta)
                .build();
    }
}
