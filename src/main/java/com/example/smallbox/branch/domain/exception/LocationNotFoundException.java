package com.example.smallbox.branch.domain.exception;

import com.example.smallbox.shared.domain.exception.NotFoundException;

public class LocationNotFoundException extends NotFoundException {
    public LocationNotFoundException(Integer id) {
        super(
                "LOCATION_NOT_FOUND",
                "Location with id " + id + " not found"
        );
    }
}
