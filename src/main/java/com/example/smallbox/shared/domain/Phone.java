package com.example.smallbox.shared.domain;

import java.util.regex.Pattern;

public record Phone(String value) {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{8}$");

    public Phone {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }

        if (!PHONE_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
    }
}
