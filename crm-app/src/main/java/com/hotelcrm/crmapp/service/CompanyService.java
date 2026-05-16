package com.hotelcrm.crmapp.service;

import com.hotelcrm.crmapp.dto.company.CompanyFilter;
import com.hotelcrm.crmapp.dto.company.CompanyRequest;
import com.hotelcrm.crmapp.dto.company.CompanySummaryDto;
import com.hotelcrm.crmapp.entity.Company;
import com.hotelcrm.crmapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyService {
    Company createCompany(CompanyRequest request, User user);
    Page<Company> getCompanies(Pageable pageable);
    Company getById(Long id);
    Page<Company> getFilteredCompanies(CompanyFilter filter, Long userId, Pageable pageable);
    List<CompanySummaryDto> fetchCompanySummaryTable(int ytdYear, int lyYear);
    Company updateCompany(Long id, CompanyFilter request, User actor);      // ← dodano actor
    void deleteCompany(Long id, User actor);                                // ← dodano actor
    void setPrimaryContact(Long companyId, Long contactId, User actor);     // ← dodano actor
}