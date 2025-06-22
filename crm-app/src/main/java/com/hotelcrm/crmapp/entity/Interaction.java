package com.hotelcrm.crmapp.entity;

import com.hotelcrm.crmapp.enums.InteractionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "interactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private InteractionType type;

    private String notes;

    private LocalDateTime interactionDate;

    @ManyToOne
    @JoinColumn(name = "contact_person_id", nullable = false)
    private ContactPerson contactPerson;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

