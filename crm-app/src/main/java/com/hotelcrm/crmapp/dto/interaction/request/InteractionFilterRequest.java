package com.hotelcrm.crmapp.dto.interaction.request;

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
    private Long companyId;
    private Long contactPersonId;

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
