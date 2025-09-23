package com.hotelcrm.crmapp.dto.event.mapper;

import com.hotelcrm.crmapp.dto.event.request.EventCreateRequest;
import com.hotelcrm.crmapp.dto.event.response.EventDetailResponse;
import com.hotelcrm.crmapp.entity.*;

public class EventMapper {

    public static Event toEntity(EventCreateRequest request, Company company, Hotel hotel, User createdBy) {
        return toEntity(request, company, hotel, createdBy, null);
    }

    public static Event toEntity(EventCreateRequest request,
                                 Company company,
                                 Hotel hotel,
                                 User createdBy,
                                 ContactPerson contact) {
        if (request == null) return null;

        return Event.builder()
                .name(request.getName())
                .description(request.getDescription())
                .type(request.getType())
                .status(request.getStatus())
                .eventDate(request.getEventDate())
                .participantsNumber(request.getParticipantsNumber() != null ? request.getParticipantsNumber() : 0)
                .estimatedTotalGrossRevenue(request.getEstimatedTotalGrossRevenue())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .company(company)
                .hotel(hotel)
                .createdBy(createdBy)
                .contactPerson(contact)
                .build();
    }

    public static EventDetailResponse toDetailResponse(Event event) {
        if (event == null) return null;

        String contactName = null;
        Long contactId = null;
        if (event.getContactPerson() != null) {
            contactId = event.getContactPerson().getId();
            String fn = event.getContactPerson().getFirstName();
            String ln = event.getContactPerson().getLastName();
            contactName = ((fn != null ? fn : "") + " " + (ln != null ? ln : "")).trim();
            if (contactName.isBlank()) contactName = null;
        }

        return EventDetailResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .type(event.getType())
                .status(event.getStatus())
                .eventDate(event.getEventDate())
                .participantsNumber(event.getParticipantsNumber())
                .estimatedTotalGrossRevenue(event.getEstimatedTotalGrossRevenue())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .companyId(event.getCompany() != null ? event.getCompany().getId() : null)
                .hotelId(event.getHotel() != null ? event.getHotel().getId() : null)
                .createdByUserId(event.getCreatedBy() != null ? event.getCreatedBy().getId() : null)
                .contactPersonId(contactId)
                .contactPersonName(contactName)
                .build();
    }
}

