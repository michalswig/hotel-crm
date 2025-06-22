package com.hotelcrm.crmapp.mapper;

import com.hotelcrm.crmapp.dto.CompanyRequest;
import com.hotelcrm.crmapp.dto.CompanyResponse;
import com.hotelcrm.crmapp.entity.Company;

public class CompanyMapper {
    public static Company toEntity(CompanyRequest request) {
        if (request == null) {
            return null;
        }
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
    public static CompanyResponse toResponse(Company company) {
        if (company == null) {
            return null;
        }
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
        response.setCreatedAt(company.getCreatedAt());
        response.setUpdatedAt(company.getUpdatedAt());
        return response;
    }
}
