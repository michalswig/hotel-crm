package com.hotelcrm.crmapp.dto.contactperson.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactPersonResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String position;
    private String email;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long companyId;
    private String companyName;
}