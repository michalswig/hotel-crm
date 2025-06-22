package com.hotelcrm.crmapp.service;

import com.hotelcrm.crmapp.dto.CompanyRequest;
import com.hotelcrm.crmapp.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService {
    Company createCompany(CompanyRequest request);
    Page<Company> getCompanies(Pageable pageable);
    Company getById(Integer id);
    Page<Company> filterCompanies(String name, String city, Pageable pageable);
}
