package com.hotelcrm.crmapp.dto.interaction.request;

import com.hotelcrm.crmapp.enums.InteractionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InteractionCreateRequest {
    @NotNull
    private InteractionType type;
    @NotNull private Long companyId;
    @NotNull private Long contactPersonId;
    @NotNull private LocalDate scheduledAt;
    @Size(max = 2000)
    private String notes;
}
