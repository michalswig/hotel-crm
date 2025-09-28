package com.hotelcrm.crmapp.dto.interaction.request;

import com.hotelcrm.crmapp.enums.InteractionStatus;
import com.hotelcrm.crmapp.enums.InteractionType;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InteractionUpdateRequest {
    private InteractionType type;
    private LocalDate scheduledAt;
    @Size(max = 2000) private String notes;
    private Long contactPersonId;
    private InteractionStatus status;
}

