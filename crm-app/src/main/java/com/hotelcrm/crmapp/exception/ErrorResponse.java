package com.hotelcrm.crmapp.exception;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private OffsetDateTime timestamp;
    private int status;
    private String error;       // short reason phrase
    private String code;        // stable machine-readable application code
    private String message;     // safe human-readable message
    private String path;        // request path
    private String traceId;     // from MDC or request attribute
    private Map<String, Object> details; // optional (e.g., field errors)
}
