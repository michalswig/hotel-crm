package com.hotelcrm.crmapp.service.impl;

import com.hotelcrm.crmapp.dto.company.CompanyFilter;
import com.hotelcrm.crmapp.dto.company.CompanyRequest;
import com.hotelcrm.crmapp.dto.company.CompanySummaryDto;
import com.hotelcrm.crmapp.entity.Company;
import com.hotelcrm.crmapp.entity.ContactPerson;
import com.hotelcrm.crmapp.entity.User;
import com.hotelcrm.crmapp.mapper.CompanyMapper;
import com.hotelcrm.crmapp.repository.CompanyRepository;
import com.hotelcrm.crmapp.repository.ContactPersonRepository;
import com.hotelcrm.crmapp.service.CompanyService;
import com.hotelcrm.crmapp.specification.CompanySpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final ContactPersonRepository contactRepo;

    @PreAuthorize("hasAnyRole('MANAGER','SPECIALIST')")
    @Transactional
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
        Specification<Company> spec = CompanySpecification.build(filter, userId);
        return companyRepository.findAll(spec, pageable);
    }

    @Override
    public List<CompanySummaryDto> fetchCompanySummaryTable(int ytdYear, int lyYear) {
        return companyRepository.fetchCompanySummaryTable(ytdYear, lyYear);
    }

    @Transactional
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
    @Transactional
    @Override
    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new EntityNotFoundException("Company " + id);
        }
        companyRepository.deleteById(id);
    }

    @Override
    public void setPrimaryContact(Long companyId, Long contactId) {
        Company c = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        ContactPerson cp = contactRepo.findByIdAndCompanyId(contactId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found in this company"));
        c.setPrimaryContactPerson(cp);
        c.setUpdatedAt(java.time.LocalDateTime.now());
    }


}
