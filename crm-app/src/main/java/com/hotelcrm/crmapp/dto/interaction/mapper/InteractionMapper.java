package com.hotelcrm.crmapp.dto.interaction.mapper;

import com.hotelcrm.crmapp.dto.interaction.request.InteractionCreateRequest;
import com.hotelcrm.crmapp.dto.interaction.response.InteractionResponse;
import com.hotelcrm.crmapp.entity.Company;
import com.hotelcrm.crmapp.entity.ContactPerson;
import com.hotelcrm.crmapp.entity.Interaction;
import com.hotelcrm.crmapp.entity.User;

import java.util.Objects;

public class InteractionMapper {

    public static Interaction toEntity(InteractionCreateRequest req, Company company, ContactPerson contact, User user) {
        return Interaction.builder()
                .type(req.getType())
                .company(company)
                .contactPerson(contact)
                .user(user)
                .scheduledAt(req.getScheduledAt())
                .notes(req.getNotes())
                .build();
    }

    public static InteractionResponse toResponse(Interaction i) {
        return InteractionResponse.builder()
                .id(i.getId())
                .type(i.getType())
                .companyId(i.getCompany() != null ? i.getCompany().getId() : null)
                .companyName(i.getCompany() != null ? i.getCompany().getName() : null)
                .contactPersonId(i.getContactPerson() != null ? i.getContactPerson().getId() : null)
                .contactPersonName(i.getContactPerson() != null ? (Objects.toString(i.getContactPerson().getFirstName(), "") + " " + Objects.toString(i.getContactPerson().getLastName(), "")).trim() : null)
                .userId(i.getUser() != null ? i.getUser().getId() : null)
                .scheduledAt(i.getScheduledAt())
                .completedAt(i.getCompletedAt())
                .followUpAt(i.getFollowUpAt())
                .notes(i.getNotes())
                .build();
    }
}
