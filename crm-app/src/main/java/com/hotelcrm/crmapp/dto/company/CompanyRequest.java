package com.hotelcrm.crmapp.dto.company;

import com.hotelcrm.crmapp.enums.Industry;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyRequest {
    @NotBlank(message = "Company name is required")
    private String name;

    @Pattern(regexp = "\\d{10}", message = "Tax ID must be 10 digits")
    private String taxId;

    @NotNull(message = "Industry must be specified")
    private Industry industry;

    @Email(message = "Invalid email address")
    private String email;

    @Pattern(regexp = "\\+?\\d{7,15}", message = "Invalid phone number")
    private String phoneNumber;

    private String website;

    private String address;

    @Pattern(regexp = "\\d{2}-\\d{3}", message = "Postal code must follow format XX-XXX")
    private String postalCode;

    private String city;

    private String country;
}
