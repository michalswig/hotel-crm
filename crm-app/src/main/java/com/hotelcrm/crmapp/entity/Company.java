package com.hotelcrm.crmapp.entity;

import com.hotelcrm.crmapp.enums.Industry;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "tax_id")
    private String taxId;
    @Enumerated(EnumType.STRING)
    private Industry industry;
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String website;
    private String address;
    @Column(name = "postal_code")
    private String postalCode;
    private String city;
    private String country;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactPerson> contactPersons;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events;

}