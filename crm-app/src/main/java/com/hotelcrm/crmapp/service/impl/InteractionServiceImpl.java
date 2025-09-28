package com.hotelcrm.crmapp.service.impl;

import com.hotelcrm.crmapp.dto.interaction.mapper.InteractionMapper;
import com.hotelcrm.crmapp.dto.interaction.request.InteractionCompleteRequest;
import com.hotelcrm.crmapp.dto.interaction.request.InteractionCreateRequest;
import com.hotelcrm.crmapp.dto.interaction.request.InteractionFilterRequest;
import com.hotelcrm.crmapp.dto.interaction.request.InteractionUpdateRequest;
import com.hotelcrm.crmapp.dto.interaction.response.InteractionResponse;
import com.hotelcrm.crmapp.entity.Company;
import com.hotelcrm.crmapp.entity.ContactPerson;
import com.hotelcrm.crmapp.entity.Interaction;
import com.hotelcrm.crmapp.entity.User;
import com.hotelcrm.crmapp.repository.CompanyRepository;
import com.hotelcrm.crmapp.repository.ContactPersonRepository;
import com.hotelcrm.crmapp.repository.InteractionRepository;
import com.hotelcrm.crmapp.service.InteractionService;
import com.hotelcrm.crmapp.specification.InteractionSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class InteractionServiceImpl implements InteractionService {

    private final InteractionRepository interactions;
    private final CompanyRepository companies;
    private final ContactPersonRepository contacts;

    @Override
    public InteractionResponse schedule(InteractionCreateRequest req, User currentUser) {
        Company company = companies.findById(req.getCompanyId())
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        ContactPerson contact = contacts.findById(req.getContactPersonId())
                .orElseThrow(() -> new EntityNotFoundException("Contact not found"));
        if (!contact.getCompany().getId().equals(company.getId())) {
            throw new IllegalArgumentException("Contact must belong to the selected company");
        }

        Interaction i = InteractionMapper.toEntity(req, company, contact, currentUser);
        i.setCreatedAt(LocalDateTime.now());
        i.setUpdatedAt(LocalDateTime.now());
        return InteractionMapper.toResponse(interactions.save(i));
    }

    @Override
    public InteractionResponse complete(Long id, InteractionCompleteRequest req, User currentUser) {
        Interaction i = interactions.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Interaction not found"));
        ensureOwner(i, currentUser);

        i.setCompletedAt(LocalDateTime.now());
        i.setFollowUpAt(req.getFollowUpAt());
        i.setNotes(req.getNotes());
        i.setUpdatedAt(LocalDateTime.now());
        return InteractionMapper.toResponse(interactions.save(i));
    }

    @Override
    public InteractionResponse update(Long id, InteractionUpdateRequest req, User currentUser) {
        Interaction i = interactions.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Interaction not found"));
        ensureOwner(i, currentUser);

        if (req.getType() != null) i.setType(req.getType());
        if (req.getNotes() != null) i.setNotes(req.getNotes());
        if (i.getCompletedAt() == null && req.getScheduledAt() != null) {
            i.setScheduledAt(req.getScheduledAt());
        }
        if (req.getContactPersonId() != null) {
            ContactPerson cp = contacts.findById(req.getContactPersonId())
                    .orElseThrow(() -> new EntityNotFoundException("Contact not found"));
            if (!cp.getCompany().getId().equals(i.getCompany().getId())) {
                throw new IllegalArgumentException("Contact must belong to the interaction's company");
            }
            i.setContactPerson(cp);
        }

        i.setUpdatedAt(LocalDateTime.now());
        return InteractionMapper.toResponse(interactions.save(i));
    }

    @Override
    public Page<InteractionResponse> getFiltered(InteractionFilterRequest filter, Pageable pageable, Long userId) {
        Specification<Interaction> spec = InteractionSpecification.build(filter, userId);
        return interactions.findAll(spec, pageable).map(InteractionMapper::toResponse);
    }

    @Override
    public Page<InteractionResponse> calendar(Long userId, LocalDate from, LocalDate to, Pageable pageable) {
        Specification<Interaction> spec = InteractionSpecification.createdBy(userId)
                .and(InteractionSpecification.scheduledBetween(from, to));
        return interactions.findAll(spec, pageable).map(InteractionMapper::toResponse);
    }

    @Override
    public List<InteractionResponse> upcomingFollowUps(Long userId) {
        return interactions.findTop50ByUser_IdAndFollowUpAtAfterOrderByFollowUpAtAsc(userId, LocalDate.now())
                .stream().map(InteractionMapper::toResponse).toList();
    }

    @Override
    public Optional<InteractionResponse> getById(Long id, User currentUser) {
        return interactions.findById(id)
                .filter(i -> i.getUser().getId().equals(currentUser.getId()))
                .map(InteractionMapper::toResponse);
    }

    @Override
    public Long delete(Long id, User currentUser) {
        Interaction i = interactions.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Interaction not found"));
        ensureOwner(i, currentUser);
        interactions.delete(i);
        return id;
    }

    private static void ensureOwner(Interaction i, User currentUser) {
        if (!i.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Forbidden");
        }
    }
}