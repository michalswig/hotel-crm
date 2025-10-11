package com.hotelcrm.crmapp.dto.event.request;

import com.hotelcrm.crmapp.enums.EventStatus;
import com.hotelcrm.crmapp.enums.EventType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventUpdateRequest {
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    private EventType type;

    private EventStatus status;

    // Allow past dates on update; creation still enforces future or present
    private LocalDateTime eventDate;

    @Min(value = 0, message = "Participants number cannot be negative")
    private Integer participantsNumber;

    @DecimalMin(value = "0.0", inclusive = true, message = "Estimated revenue must be positive")
    private BigDecimal estimatedTotalGrossRevenue;

    private LocalDateTime updatedAt;

    private Long companyId;

    private Long hotelId;

    private Long createdByUserId;

    private Long contactPersonId;

    public EventFilterRequest toFilterRequest() {
        return EventFilterRequest.builder()
                .name(this.name)
                .description(this.description)
                .type(this.type)
                .status(this.status)
                .eventDate(this.eventDate)
                .participantsNumber(this.participantsNumber)
                .estimatedTotalGrossRevenue(this.estimatedTotalGrossRevenue)
                .updatedAt(this.updatedAt != null ? this.updatedAt : LocalDateTime.now())
                .companyId(this.companyId)
                .hotelId(this.hotelId)
                .createdByUserId(this.createdByUserId)
                .contactPersonId(this.contactPersonId)
                .build();
    }

}
