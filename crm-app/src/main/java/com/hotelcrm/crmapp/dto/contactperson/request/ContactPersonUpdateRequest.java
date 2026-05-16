package com.hotelcrm.crmapp.dto.contactperson.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactPersonUpdateRequest {

    private String firstName;

    private String lastName;

    private String position;

    @Email(message = "Invalid email address")
    private String email;

    @Pattern(regexp = "\\+?\\d{7,15}", message = "Invalid phone number")
    private String phoneNumber;

    private Long companyId;
}
