package com.hotelcrm.crmapp.dto.interaction.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InteractionCompleteRequest {
    @NotNull
    private LocalDateTime followUpAt;
    @NotBlank
    @Size(max = 2000) private String notes;
}
