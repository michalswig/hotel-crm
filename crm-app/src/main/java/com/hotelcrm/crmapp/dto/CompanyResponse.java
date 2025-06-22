package com.hotelcrm.crmapp.dto;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
