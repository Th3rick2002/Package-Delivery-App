package com.example.smallbox.shared.domain;

import com.example.smallbox.shared.domain.exception.InvalidEmailFormatException;
import java.util.regex.Pattern;

public record Email(String value) {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");

    public Email(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidEmailFormatException(value);
        }

        value = value.trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new InvalidEmailFormatException(value);
        }
        this.value = value;
    }

    public String getDomain() {
        return value.substring(value.indexOf("@") + 1);
    }

    public String getLocalPart() {
        return value.substring(0, value.indexOf("@"));
    }

    public static Email of(String email) {
        return new Email(email);
    }
}
