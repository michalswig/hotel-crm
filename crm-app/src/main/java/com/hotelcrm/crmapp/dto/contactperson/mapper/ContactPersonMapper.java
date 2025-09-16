package com.hotelcrm.crmapp.dto.contactperson.mapper;

import com.hotelcrm.crmapp.dto.contactperson.request.ContactPersonRequest;
import com.hotelcrm.crmapp.dto.contactperson.request.ContactPersonUpdateRequest;
import com.hotelcrm.crmapp.dto.contactperson.response.ContactPersonResponse;
import com.hotelcrm.crmapp.entity.Company;
import com.hotelcrm.crmapp.entity.ContactPerson;

public class ContactPersonMapper {

    public static ContactPerson toEntity(ContactPersonRequest request, Company company) {
        if (request == null) {
            return null;
        }
        ContactPerson contact = new ContactPerson();
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setPosition(request.getPosition());
        contact.setEmail(request.getEmail());
        contact.setPhoneNumber(request.getPhoneNumber());
        contact.setCompany(company);
        return contact;
    }

    public static void updateEntity(ContactPerson contact, ContactPersonUpdateRequest request, Company company) {
        if (request == null) {
            return;
        }
        if (request.getFirstName() != null) {
            contact.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            contact.setLastName(request.getLastName());
        }
        if (request.getPosition() != null) {
            contact.setPosition(request.getPosition());
        }
        if (request.getEmail() != null) {
            contact.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            contact.setPhoneNumber(request.getPhoneNumber());
        }
        if (company != null) {
            contact.setCompany(company);
        }
    }

    public static ContactPersonResponse toResponse(ContactPerson contact) {
        if (contact == null) {
            return null;
        }
        return ContactPersonResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .position(contact.getPosition())
                .email(contact.getEmail())
                .phoneNumber(contact.getPhoneNumber())
                .createdAt(contact.getCreatedAt())
                .updatedAt(contact.getUpdatedAt())
                .companyId(contact.getCompany() != null ? contact.getCompany().getId() : null)
                .companyName(contact.getCompany() != null ? contact.getCompany().getName() : null)
                .build();
    }
    public static void update(ContactPerson contact, ContactPersonRequest req) {
        contact.setFirstName(req.getFirstName());
        contact.setLastName(req.getLastName());
        contact.setPosition(req.getPosition());
        contact.setEmail(req.getEmail());
        contact.setPhoneNumber(req.getPhoneNumber());
    }
}