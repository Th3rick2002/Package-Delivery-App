package com.example.smallbox.shared.infrastructure.persistence.entities;

import com.example.smallbox.shared.infrastructure.persistence.CountryJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "department")
@Getter
@Setter
public class DepartmentJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_department")
    private Integer idDepartment;

    @Column(name = "deparment_name", nullable = false)
    private String deparmentName;

    @Column(name = "delivery_zone_code")
    private String deliveryZoneCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private CountryJpaEntity country;
}
