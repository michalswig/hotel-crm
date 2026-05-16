package com.hotelcrm.crmapp.dto.interaction.request;

import com.hotelcrm.crmapp.enums.InteractionStatus;
import com.hotelcrm.crmapp.enums.InteractionType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InteractionFilterRequest {

    private Long id;
    private InteractionType type;
    private InteractionStatus status;
    private Long companyId;
    private Long contactPersonId;
    private String notes;

    private String q;

    private LocalDateTime scheduledFrom;
    private LocalDateTime scheduledTo;

    private LocalDateTime completedFrom;
    private LocalDateTime completedTo;

    private LocalDateTime followUpFrom;
    private LocalDateTime followUpTo;

    private Boolean pendingOnly;
    private Boolean completedOnly;
    private Boolean withFollowUpOnly;
}
