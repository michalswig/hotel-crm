package com.hotelcrm.crmapp.service.impl;

import com.hotelcrm.crmapp.dto.contactperson.mapper.ContactPersonMapper;
import com.hotelcrm.crmapp.dto.contactperson.request.ContactPersonRequest;
import com.hotelcrm.crmapp.dto.contactperson.response.ContactPersonResponse;
import com.hotelcrm.crmapp.entity.Company;
import com.hotelcrm.crmapp.entity.ContactPerson;
import com.hotelcrm.crmapp.repository.CompanyRepository;
import com.hotelcrm.crmapp.repository.ContactPersonRepository;
import com.hotelcrm.crmapp.service.ContactPersonService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ContactPersonServiceImpl implements ContactPersonService {

    private final CompanyRepository companyRepo;
    private final ContactPersonRepository contactRepo;

    @Override
    public Page<ContactPersonResponse> list(Long companyId, Pageable pageable) {
        return contactRepo.findByCompanyId(companyId, pageable).map(ContactPersonMapper::toResponse);
    }

    @PreAuthorize("hasAnyRole('MANAGER','SPECIALIST')")
    @Override
    public ContactPersonResponse create(Long companyId, ContactPersonRequest req) {
        if (contactRepo.existsByCompanyIdAndEmailIgnoreCase(companyId, req.getEmail()))
            throw new IllegalArgumentException("Email already exists in this company");

        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        ContactPerson cp = ContactPersonMapper.toEntity(req, company);
        cp.setCreatedAt(LocalDateTime.now());
        company.addContact(cp);

        contactRepo.save(cp);
        return ContactPersonMapper.toResponse(cp);
    }

    @PreAuthorize("hasAnyRole('MANAGER','SPECIALIST')")
    @Override
    public ContactPersonResponse update(Long companyId, Long contactId, ContactPersonRequest req) {
        ContactPerson cp = contactRepo.findByIdAndCompanyId(contactId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found"));

        if (!cp.getEmail().equalsIgnoreCase(req.getEmail())
                && contactRepo.existsByCompanyIdAndEmailIgnoreCase(companyId, req.getEmail())) {
            throw new IllegalArgumentException("Email already exists in this company");
        }

        ContactPersonMapper.update(cp, req);
        cp.setUpdatedAt(LocalDateTime.now());
        return ContactPersonMapper.toResponse(cp);
    }

    @PreAuthorize("hasAnyRole('MANAGER','SPECIALIST')")
    @Override
    public void delete(Long companyId, Long contactId) {
        Company c = companyRepo.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        ContactPerson cp = contactRepo.findByIdAndCompanyId(contactId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found"));
        c.removeContact(cp);
        contactRepo.delete(cp);
        c.setUpdatedAt(LocalDateTime.now());
    }
}
