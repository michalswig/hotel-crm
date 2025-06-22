package com.hotelcrm.crmapp.service.impl;

import com.hotelcrm.crmapp.dto.CompanyRequest;
import com.hotelcrm.crmapp.entity.Company;
import com.hotelcrm.crmapp.mapper.CompanyMapper;
import com.hotelcrm.crmapp.repository.CompanyRepository;
import com.hotelcrm.crmapp.service.CompanyService;
import com.hotelcrm.crmapp.specification.CompanySpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    public Company createCompany(CompanyRequest request) {
        Company company = CompanyMapper.toEntity(request);
        return companyRepository.save(company);
    }

    @Override
    public Page<Company> getCompanies(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    @Override
    public Company getById(Integer id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
    }

    @Override
    public Page<Company> filterCompanies(String name, String city, Pageable pageable) {
        Specification<Company> spec = Specification.where(null);

        if (name != null) {
            spec = spec.and(CompanySpecification.hasName(name));
        }

        return companyRepository.findAll(spec, pageable);

    }
}
