package com.hotelcrm.crmapp.entity;


import com.hotelcrm.crmapp.enums.EventStatus;
import com.hotelcrm.crmapp.enums.EventType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EventType type;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EventStatus status;
    @Column(name = "participants_number")
    private int participantsNumber;
    @Column(name = "estimated_total_gross_revenue")
    private BigDecimal estimatedTotalGrossRevenue;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}
