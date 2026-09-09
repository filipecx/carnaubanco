package com.carnaubanco.userservice.dto;

import java.time.OffsetDateTime;
import java.util.Map;

public record ErrorResponse(
    int status,
    String message,
    OffsetDateTime timestamp,
    Map<String, String> fieldErrors
) {
    public ErrorResponse(int status, String message, OffsetDateTime timestamp) {
        this(status, message, timestamp, null);
    }
}
