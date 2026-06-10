package com.example.smallbox.shared.domain;

import com.example.smallbox.shared.domain.exception.InvalidPhoneFormatException;
import java.util.regex.Pattern;

public record Phone(String value) {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{8}$");

    public Phone {
        if (value == null || value.isBlank()) {
            throw new InvalidPhoneFormatException(value);
        }

        if (!PHONE_PATTERN.matcher(value).matches()) {
            throw new InvalidPhoneFormatException(value);
        }
    }
}
