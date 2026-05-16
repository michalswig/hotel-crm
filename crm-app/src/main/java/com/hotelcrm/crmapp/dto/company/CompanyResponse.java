package com.hotelcrm.crmapp.dto.company;

import com.hotelcrm.crmapp.enums.Industry;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CompanyResponse {
    private Long id;
    private String name;
    private String taxId;
    private Industry industry;
    private String email;
    private String phoneNumber;
    private String website;
    private String address;
    private String postalCode;
    private String city;
    private String country;
    private Long createdByUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long primaryContactId;
    private String primaryContactName;
    private String primaryContactEmail;

}
