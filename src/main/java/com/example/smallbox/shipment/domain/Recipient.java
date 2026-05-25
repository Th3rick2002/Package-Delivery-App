package com.example.smallbox.shipment.domain;

import com.example.smallbox.shared.domain.Email;
import com.example.smallbox.shared.domain.Phone;

import java.util.UUID;
import java.util.regex.Pattern;

public record Recipient(
        UUID recipientId,
        String firstName,
        String secondName,
        String lastname,
        String secondLastName,
        Phone phone,
        Email email
) {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");

    public Recipient {
        firstName = validateName(firstName, "First name", true);
        secondName = validateName(secondName, "Second name", false);
        lastname = validateName(lastname, "Last name", true);
        secondLastName = validateName(secondLastName, "Second last name", false);

        if (phone == null) throw new IllegalArgumentException("Phone is required");
        if (email == null) throw new IllegalArgumentException("Email is required");
    }

    private static String validateName(String name, String fieldName, boolean isRequired) {
        if (name == null || name.isBlank()) {
            if (isRequired) throw new IllegalArgumentException(fieldName + " cannot be empty");
            return null;
        }

        String cleanedName = name.trim();

        if (cleanedName.length() < 3)
            throw new IllegalArgumentException(fieldName + " must be at least 3 characters");

        if (!NAME_PATTERN.matcher(cleanedName).matches())
            throw new IllegalArgumentException(fieldName + " contains invalid characters");

        return cleanedName.toUpperCase();
    }

    public static Recipient create(
            String firstName,
            String secondName,
            String lastname,
            String secondLastName,
            Phone phone,
            Email email
    ) {
        return new Recipient(null, firstName, secondName, lastname, secondLastName, phone, email);
    }

    public static Recipient withId(
            UUID id,
            String firstName,
            String secondName,
            String lastname,
            String secondLastName,
            Phone phone,
            Email email
    ) {
        if (id == null) throw new IllegalArgumentException("ID is required for an existing recipient");
        return new Recipient(id, firstName, secondName, lastname, secondLastName, phone, email);
    }

    public String getFullName() {
        StringBuilder sb = new StringBuilder(firstName);
        if (secondName != null) sb.append(" ").append(secondName);
        sb.append(" ").append(lastname);
        if (secondLastName != null) sb.append(" ").append(secondLastName);
        return sb.toString();
    }
}
