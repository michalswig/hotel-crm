package com.hotelcrm.crmapp.dto.contactperson.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactPersonFilter {
    private String firstName;
    private String lastName;
    private String position;
    private String email;
    private String phoneNumber;

    private Long companyId;
    private String companyName;
}
