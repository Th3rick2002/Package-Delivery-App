package com.example.smallbox.shared.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "countries")
@Getter
@Setter
public class CountryJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Integer countryId;

    @Column(name = "country_name", unique = true, nullable = false)
    private String countryName;

    @Column(name = "iso_code", unique = true, nullable = false, columnDefinition = "bpchar(2)")
    private String isoCode;
}
