package com.hotelcrm.crmapp.mapper;

import com.hotelcrm.crmapp.dto.company.CompanyRequest;
import com.hotelcrm.crmapp.dto.company.CompanyResponse;
import com.hotelcrm.crmapp.entity.Company;
import com.hotelcrm.crmapp.entity.ContactPerson;

public class CompanyMapper {

    public static Company toEntity(CompanyRequest request) {
        if (request == null) return null;
        Company company = new Company();
        company.setName(request.getName());
        company.setTaxId(request.getTaxId());
        company.setIndustry(request.getIndustry());
        company.setEmail(request.getEmail());
        company.setPhoneNumber(request.getPhoneNumber());
        company.setWebsite(request.getWebsite());
        company.setAddress(request.getAddress());
        company.setPostalCode(request.getPostalCode());
        company.setCity(request.getCity());
        company.setCountry(request.getCountry());
        return company;
    }

    /** Używaj w listach – szybkie, bez dogrywania pól kontaktu (unik N+1). */
    public static CompanyResponse toResponse(Company company) {
        if (company == null) return null;

        CompanyResponse response = new CompanyResponse();
        response.setId(company.getId());
        response.setName(company.getName());
        response.setTaxId(company.getTaxId());
        response.setIndustry(company.getIndustry());
        response.setEmail(company.getEmail());
        response.setPhoneNumber(company.getPhoneNumber());
        response.setWebsite(company.getWebsite());
        response.setAddress(company.getAddress());
        response.setPostalCode(company.getPostalCode());
        response.setCity(company.getCity());
        response.setCountry(company.getCountry());
        response.setCreatedByUserId(
                company.getCreatedBy() != null ? company.getCreatedBy().getId() : null);
        response.setCreatedAt(company.getCreatedAt());
        response.setUpdatedAt(company.getUpdatedAt());

        ContactPerson pc = company.getPrimaryContactPerson();
        if (pc != null) {
            response.setPrimaryContactId(pc.getId()); // id nie inicjalizuje proxy w Hibernate
        }
        return response;
    }

    /** Używaj w szczegółach firmy – dorzuca nazwę i e-mail „primary”. */
    public static CompanyResponse toDetailResponse(Company company) {
        CompanyResponse r = toResponse(company);
        ContactPerson pc = company.getPrimaryContactPerson();
        if (pc != null) {
            // To może zainicjalizować proxy – OK w szczegółach (i tak pobierasz jedną firmę)
            r.setPrimaryContactName(pc.getFirstName() + " " + pc.getLastName());
            r.setPrimaryContactEmail(pc.getEmail());
        }
        return r;
    }
}