package com.hotelcrm.crmapp.dto.event.response;

import com.hotelcrm.crmapp.enums.EventStatus;
import com.hotelcrm.crmapp.enums.EventType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDetailResponse {
    private Long id;
    private String name;
    private String description;
    private EventType type;
    private EventStatus status;
    private LocalDateTime eventDate;
    private Integer participantsNumber;
    private BigDecimal estimatedTotalGrossRevenue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long companyId;
    private Long hotelId;
    private Long createdByUserId;
}
