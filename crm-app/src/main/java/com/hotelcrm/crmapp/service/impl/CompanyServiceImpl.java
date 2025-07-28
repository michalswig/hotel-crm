package com.hotelcrm.crmapp.service.impl;

import com.hotelcrm.crmapp.dto.CompanyFilter;
import com.hotelcrm.crmapp.dto.CompanyRequest;
import com.hotelcrm.crmapp.dto.CompanySummaryDto;
import com.hotelcrm.crmapp.entity.Company;
import com.hotelcrm.crmapp.entity.User;
import com.hotelcrm.crmapp.mapper.CompanyMapper;
import com.hotelcrm.crmapp.repository.CompanyRepository;
import com.hotelcrm.crmapp.service.CompanyService;
import com.hotelcrm.crmapp.specification.CompanySpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @PreAuthorize("hasAnyRole('MANAGER','SPECIALIST')")
    @Override
    public Company createCompany(CompanyRequest request, User creator) {

        Company company = CompanyMapper.toEntity(request);
        company.setCreatedBy(creator);
        company.setCreatedAt(LocalDateTime.now());

        return companyRepository.save(company);
    }

    @Override
    public Page<Company> getCompanies(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    @Override
    public Company getById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
    }

    @Override
    public Page<Company> getFilteredCompanies(CompanyFilter filter, Long userId, Pageable pageable) {

        Specification<Company> spec = Specification
                .where(CompanySpecification.createdBy(userId));

        if (filter.getName() != null) {
            spec = spec.and(CompanySpecification.hasName(filter.getName()));
        }
        if (filter.getId() != null) {
            spec = spec.and(CompanySpecification.createdBy(filter.getId()));
        }
        return companyRepository.findAll(spec, pageable);
    }

    @Override
    public List<CompanySummaryDto> fetchCompanySummaryTable(int ytdYear, int lyYear) {
        return companyRepository.fetchCompanySummaryTable(ytdYear, lyYear);
    }

    @Override
    public Company updateCompany(Long id, CompanyFilter filter) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company " + id));

        if (filter.getName() != null) company.setName(filter.getName());
        if (filter.getTaxId() != null) company.setTaxId(filter.getTaxId());
        if (filter.getIndustry() != null) company.setIndustry(filter.getIndustry());
        if (filter.getEmail() != null) company.setEmail(filter.getEmail());
        if (filter.getPhoneNumber() != null) company.setPhoneNumber(filter.getPhoneNumber());
        if (filter.getWebsite() != null) company.setWebsite(filter.getWebsite());
        if (filter.getAddress() != null) company.setAddress(filter.getAddress());
        if (filter.getPostalCode() != null) company.setPostalCode(filter.getPostalCode());
        if (filter.getCity() != null) company.setCity(filter.getCity());
        if (filter.getCountry() != null) company.setCountry(filter.getCountry());

        company.setUpdatedAt(LocalDateTime.now());
        return companyRepository.save(company);
    }

    @PreAuthorize("hasAnyRole('MANAGER','SPECIALIST')")
    @Override
    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new EntityNotFoundException("Company " + id);
        }
        companyRepository.deleteById(id);
    }


}
