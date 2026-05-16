package com.hotelcrm.crmapp.service.impl;

import com.hotelcrm.crmapp.dto.contactperson.mapper.ContactPersonMapper;
import com.hotelcrm.crmapp.dto.contactperson.request.ContactPersonRequest;
import com.hotelcrm.crmapp.dto.contactperson.response.ContactPersonResponse;
import com.hotelcrm.crmapp.entity.Company;
import com.hotelcrm.crmapp.entity.ContactPerson;
import com.hotelcrm.crmapp.entity.User;
import com.hotelcrm.crmapp.exception.ForbiddenException;
import com.hotelcrm.crmapp.repository.CompanyRepository;
import com.hotelcrm.crmapp.repository.ContactPersonRepository;
import com.hotelcrm.crmapp.service.ContactPersonService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ContactPersonServiceImpl implements ContactPersonService {

    private final CompanyRepository companyRepo;
    private final ContactPersonRepository contactRepo;

    // Kontakt należy do firmy — sprawdzamy czy aktor może modyfikować firmę-właściciela.
    // Właściciel firmy (createdBy) lub Manager/Administrator — mogą.
    private void assertCanModify(Company company, User actor) {
        boolean isOwner = company.getCreatedBy().getId().equals(actor.getId());
        String roleName = actor.getRole().getName().name();
        boolean isManagerOrAdmin = roleName.equals("MANAGER") || roleName.equals("ADMINISTRATOR");

        if (!isOwner && !isManagerOrAdmin) {
            throw new ForbiddenException("You don't have permission to modify contacts in this company");
        }
    }

    @Override
    public Page<ContactPersonResponse> list(Long companyId, Pageable pageable) {
        return contactRepo.findByCompanyId(companyId, pageable).map(ContactPersonMapper::toResponse);
    }

    @Override
    public ContactPersonResponse create(Long companyId, ContactPersonRequest req, User actor) {
        if (contactRepo.existsByCompanyIdAndEmailIgnoreCase(companyId, req.getEmail()))
            throw new IllegalArgumentException("Email already exists in this company");

        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        assertCanModify(company, actor);

        ContactPerson cp = ContactPersonMapper.toEntity(req, company);
        cp.setCreatedAt(LocalDateTime.now());
        company.addContact(cp);

        contactRepo.save(cp);
        return ContactPersonMapper.toResponse(cp);
    }

    @Override
    public ContactPersonResponse update(Long companyId, Long contactId, ContactPersonRequest req, User actor) {
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        assertCanModify(company, actor);

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

    @Override
    public void delete(Long companyId, Long contactId, User actor) {
        Company c = companyRepo.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        assertCanModify(c, actor);

        ContactPerson cp = contactRepo.findByIdAndCompanyId(contactId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found"));

        c.removeContact(cp);
        contactRepo.delete(cp);
        c.setUpdatedAt(LocalDateTime.now());
    }
}