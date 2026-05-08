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
import com.hotelcrm.crmapp.enums.InteractionStatus;
import com.hotelcrm.crmapp.exception.ForbiddenException;
import com.hotelcrm.crmapp.exception.NotFoundException;
import com.hotelcrm.crmapp.repository.CompanyRepository;
import com.hotelcrm.crmapp.repository.ContactPersonRepository;
import com.hotelcrm.crmapp.repository.InteractionRepository;
import com.hotelcrm.crmapp.service.InteractionService;
import com.hotelcrm.crmapp.specification.InteractionSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class InteractionServiceImpl implements InteractionService {

    private final InteractionRepository interactionRepository;
    private final CompanyRepository companyRepository;
    private final ContactPersonRepository contactPersonRepository;

    // Właściciel (user) lub Manager/Administrator mogą modyfikować.
    // Specialist który nie jest właścicielem — dostaje 403.
    private void assertCanModify(Interaction i, User actor) {
        boolean isOwner = i.getUser().getId().equals(actor.getId());
        String roleName = actor.getRole().getName().name();
        boolean isManagerOrAdmin = roleName.equals("MANAGER") || roleName.equals("ADMINISTRATOR");

        if (!isOwner && !isManagerOrAdmin) {
            throw new ForbiddenException("You don't have permission to modify this interaction");
        }
    }

    @Override
    public InteractionResponse create(InteractionCreateRequest req, User currentUser) {
        Company company = companyRepository.findById(req.getCompanyId())
                .orElseThrow(() -> new NotFoundException("Company not found"));
        ContactPerson contact = contactPersonRepository.findById(req.getContactPersonId())
                .orElseThrow(() -> new NotFoundException("Contact not found"));
        if (!contact.getCompany().getId().equals(company.getId())) {
            throw new IllegalArgumentException("Contact must belong to the selected company");
        }

        Interaction i = InteractionMapper.toEntity(req, company, contact, currentUser);
        i.setStatus(InteractionStatus.PLANNED);
        i.setCreatedAt(LocalDateTime.now());
        i.setUpdatedAt(LocalDateTime.now());
        return InteractionMapper.toResponse(interactionRepository.save(i));
    }

    @Override
    public InteractionResponse complete(Long id, InteractionCompleteRequest req, User currentUser) {
        Interaction i = interactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Interaction not found"));

        assertCanModify(i, currentUser);

        i.setCompletedAt(LocalDateTime.now());
        i.setStatus(InteractionStatus.DONE);
        i.setFollowUpAt(req.getFollowUpAt());
        i.setNotes(req.getNotes());
        i.setUpdatedAt(LocalDateTime.now());
        return InteractionMapper.toResponse(interactionRepository.save(i));
    }

    @Override
    public InteractionResponse update(Long id, InteractionUpdateRequest req, User currentUser) {
        Interaction i = interactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Interaction not found"));

        assertCanModify(i, currentUser);

        if (req.getType() != null) i.setType(req.getType());
        if (req.getNotes() != null) i.setNotes(req.getNotes());
        if (i.getCompletedAt() == null && req.getScheduledAt() != null) {
            i.setScheduledAt(req.getScheduledAt());
        }
        if (req.getContactPersonId() != null) {
            ContactPerson cp = contactPersonRepository.findById(req.getContactPersonId())
                    .orElseThrow(() -> new NotFoundException("Contact not found"));
            if (!cp.getCompany().getId().equals(i.getCompany().getId())) {
                throw new IllegalArgumentException("Contact must belong to the interaction's company");
            }
            i.setContactPerson(cp);
        }

        if (req.getStatus() != null) {
            InteractionStatus newStatus = req.getStatus();
            if (newStatus != InteractionStatus.OVERDUE) {
                if (newStatus == InteractionStatus.DONE) {
                    if (i.getCompletedAt() == null) {
                        i.setCompletedAt(LocalDateTime.now());
                    }
                } else {
                    i.setCompletedAt(null);
                }
                i.setStatus(newStatus);
            }
        }

        i.setUpdatedAt(LocalDateTime.now());
        return InteractionMapper.toResponse(interactionRepository.save(i));
    }

    @Override
    public Page<InteractionResponse> getFiltered(InteractionFilterRequest filter, Pageable pageable, Long userId) {
        Specification<Interaction> spec = InteractionSpecification.build(filter, userId);
        return interactionRepository.findAll(spec, pageable).map(InteractionMapper::toResponse);
    }

    @Override
    public Page<InteractionResponse> calendar(Long userId, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        Specification<Interaction> spec = InteractionSpecification.createdBy(userId)
                .and(InteractionSpecification.scheduledBetween(from, to));
        return interactionRepository.findAll(spec, pageable).map(InteractionMapper::toResponse);
    }

    @Override
    public List<InteractionResponse> upcomingFollowUps(Long userId) {
        return interactionRepository
                .findTop50ByUser_IdAndFollowUpAtAfterOrderByFollowUpAtAsc(userId, LocalDateTime.now())
                .stream().map(InteractionMapper::toResponse).toList();
    }

    @Override
    public Optional<InteractionResponse> getById(Long id, User currentUser) {
        return interactionRepository.findById(id)
                .filter(i -> i.getUser().getId().equals(currentUser.getId()))
                .map(InteractionMapper::toResponse);
    }

    @Override
    public Long delete(Long id, User currentUser) {
        Interaction i = interactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Interaction not found"));

        assertCanModify(i, currentUser);

        interactionRepository.delete(i);
        return id;
    }
}