package com.example.smallbox.shared.infrastructure.persistence.entities;

import com.example.smallbox.shared.infrastructure.persistence.CountryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCountryRepository extends JpaRepository<CountryJpaEntity, Integer> {
}
