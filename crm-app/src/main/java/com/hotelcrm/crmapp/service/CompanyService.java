package com.hotelcrm.crmapp.service;

import com.hotelcrm.crmapp.dto.CompanyFilter;
import com.hotelcrm.crmapp.dto.CompanyRequest;
import com.hotelcrm.crmapp.dto.CompanySummaryDto;
import com.hotelcrm.crmapp.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyService {
    Company createCompany(CompanyRequest request);

    Page<Company> getCompanies(Pageable pageable);

    Company getById(Long id);

    Page<Company> filterCompanies(CompanyFilter filter, Long userId, Pageable pageable);

    List<CompanySummaryDto> fetchCompanySummaryTable(int ytdYear, int lyYear);

    Company updateCompany(Long id, CompanyFilter request);

    void deleteCompany(Long id);

}
