package com.example.smallbox.shared.domain.port;

import com.example.smallbox.shared.domain.LocationId;
import com.example.smallbox.shared.domain.LocationInfo;

import java.util.Optional;

public interface LocationRepository {
    boolean exists(LocationId locationId);

    Optional<LocationInfo> findById(LocationId locationId);
}
