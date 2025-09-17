package com.hotelcrm.crmapp.dto.event.request;

import com.hotelcrm.crmapp.enums.EventStatus;
import com.hotelcrm.crmapp.enums.EventType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCreateRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @NotNull(message = "Event type is required")
    private EventType type;

    @NotNull(message = "Event status is required")
    private EventStatus status;

    @NotNull(message = "Event date is required")
    @FutureOrPresent(message = "Event date must be in the present or future")
    private LocalDateTime eventDate;

    @Min(value = 0, message = "Participants number cannot be negative")
    private Integer participantsNumber;

    @DecimalMin(value = "0.0", inclusive = true, message = "Estimated revenue must be positive")
    private BigDecimal estimatedTotalGrossRevenue;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @NotNull(message = "Company ID is required")
    private Long companyId;

    private Long hotelId;

    private Long contactPersonId;
}
