package com.hotelcrm.crmapp.dto.interaction.request;

import com.hotelcrm.crmapp.enums.InteractionType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InteractionFilterRequest {

    private Long id;
    private InteractionType type;
    private Long companyId;
    private Long contactPersonId;

    private LocalDate scheduledFrom;
    private LocalDate scheduledTo;

    private LocalDateTime completedFrom;
    private LocalDateTime completedTo;

    private LocalDate followUpFrom;
    private LocalDate followUpTo;

    private Boolean pendingOnly;
    private Boolean completedOnly;
    private Boolean withFollowUpOnly;
}
