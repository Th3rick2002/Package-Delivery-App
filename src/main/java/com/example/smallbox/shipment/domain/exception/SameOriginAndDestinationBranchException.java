package com.example.smallbox.shipment.domain.exception;

import com.example.smallbox.shared.domain.exception.UnprocessableEntityException;

public class SameOriginAndDestinationBranchException extends UnprocessableEntityException {
    public SameOriginAndDestinationBranchException() {
        super("SAME_ORIGIN_AND_DESTINATION_BRANCH", "Origin and destination branch cannot be the same");
    }
}
