package com.hotelcrm.crmapp.dto.interaction.response;

import com.hotelcrm.crmapp.enums.InteractionType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InteractionResponse {
    private Long id;
    private InteractionType type;
    private Long companyId;
    private String companyName;
    private Long contactPersonId;
    private String contactPersonName;
    private Long userId;
    private LocalDateTime scheduledAt;
    private LocalDateTime completedAt;
    private LocalDateTime followUpAt;
    private String notes;
}
