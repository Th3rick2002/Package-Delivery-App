package com.example.smallbox.shipment.domain.port;

import com.example.smallbox.shipment.domain.Recipient;

import java.util.Optional;
import java.util.UUID;

public interface RecipientRepository {
    Recipient save(Recipient recipient);
    Optional<Recipient> findById(UUID id);
}
