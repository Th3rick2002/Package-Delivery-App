package com.example.smallbox.shipment.domain.vo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record TrackingNumber(String value) {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final Pattern VALIDATION_PATTERN =
            Pattern.compile("^([A-Z]{2,4})-(\\d{8})-(\\d{12,14})$");

    public TrackingNumber {
        if (value == null || !VALIDATION_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Invalid tracking number format. Expected format: XX-YYYYMMDD-HHMMSSmmmmmm"
            );
        }
    }

    public static TrackingNumber create(String prefix) {
        validatePrefix(prefix);

        LocalDateTime now = LocalDateTime.now();
        String sequence = String.format("%02d%02d%02d%06d",
                now.getHour(),
                now.getMinute(),
                now.getSecond(),
                now.getNano() / 1000
        );

        String value = String.format("%s-%s-%s",
                prefix.toUpperCase(),
                now.format(DATE_FORMATTER),
                sequence
        );

        return new TrackingNumber(value);
    }

    public static TrackingNumber fromPersisted(String value) {
        return new TrackingNumber(value);
    }

    private static void validatePrefix(String prefix) {
        if (prefix == null || !prefix.matches("^[A-Za-z]{2,4}$")) {
            throw new IllegalArgumentException("Prefix must be 2-4 letters");
        }
    }

    public String getPrefix() {
        Matcher matches = VALIDATION_PATTERN.matcher(value);
        return matches.find() ? matches.group(1) : "";
    }

    public String getDate() {
        Matcher matcher = VALIDATION_PATTERN.matcher(value);
        return matcher.find() ? matcher.group(2) : "";
    }
}
