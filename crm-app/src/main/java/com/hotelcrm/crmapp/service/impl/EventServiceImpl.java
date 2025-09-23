package com.hotelcrm.crmapp.service.impl;

import com.hotelcrm.crmapp.dto.event.mapper.EventMapper;
import com.hotelcrm.crmapp.dto.event.request.EventCreateRequest;
import com.hotelcrm.crmapp.dto.event.request.EventFilterRequest;
import com.hotelcrm.crmapp.dto.event.request.EventUpdateRequest;
import com.hotelcrm.crmapp.entity.*;
import com.hotelcrm.crmapp.repository.CompanyRepository;
import com.hotelcrm.crmapp.repository.ContactPersonRepository;
import com.hotelcrm.crmapp.repository.EventRepository;
import com.hotelcrm.crmapp.repository.HotelRepository;
import com.hotelcrm.crmapp.service.EventService;
import com.hotelcrm.crmapp.specification.EventSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CompanyRepository companyRepository;
    private final HotelRepository hotelRepository;
    private final ContactPersonRepository contactPersonRepository;

    @Override
    public Event create(Event event, User user) {
        event.setCreatedBy(user);
        if (event.getCreatedAt() == null) event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());
        return eventRepository.save(event);
    }

    @Override
    public Event create(EventCreateRequest req, User currentUser) {
        Hotel hotel = currentUser.getHotel();
        if (hotel == null) {
            throw new IllegalStateException("Authenticated user has no assigned hotel");
        }

        Company company = companyRepository.findById(req.getCompanyId())
                .orElseThrow(() -> new EntityNotFoundException("Company not found: " + req.getCompanyId()));

        ContactPerson contact = null;
        if (req.getContactPersonId() != null) {
            contact = contactPersonRepository
                    .findByIdAndCompanyId(req.getContactPersonId(), req.getCompanyId())
                    .orElseThrow(() -> new IllegalArgumentException("Contact does not belong to the selected company"));
        } else {
            contact = company.getPrimaryContactPerson();
        }

        Event event = EventMapper.toEntity(req, company, hotel, currentUser, contact);
        if (event.getCreatedAt() == null) event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());
        return eventRepository.save(event);
    }

    @Override
    public Page<Event> getEvents(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    @Override
    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    @Override
    public Page<Event> getFiltered(EventFilterRequest filter, Pageable pageable, Long userId) {
        return eventRepository.findAll(EventSpecification.build(filter, userId), pageable);
    }

    @Override
    public Event update(EventFilterRequest request, Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event with ID " + id + " not found"));

        if (request.getName() != null) event.setName(request.getName());
        if (request.getDescription() != null) event.setDescription(request.getDescription());
        if (request.getType() != null) event.setType(request.getType());
        if (request.getStatus() != null) event.setStatus(request.getStatus());
        if (request.getEventDate() != null) event.setEventDate(request.getEventDate());
        if (request.getParticipantsNumber() != null) event.setParticipantsNumber(request.getParticipantsNumber());
        if (request.getEstimatedTotalGrossRevenue() != null) event.setEstimatedTotalGrossRevenue(request.getEstimatedTotalGrossRevenue());

        event.setUpdatedAt(LocalDateTime.now());
        return eventRepository.save(event);
    }

    @Override
    public Event update(Long id, EventUpdateRequest req) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event with ID " + id + " not found"));

        if (req.getName() != null) event.setName(req.getName());
        if (req.getDescription() != null) event.setDescription(req.getDescription());
        if (req.getType() != null) event.setType(req.getType());
        if (req.getStatus() != null) event.setStatus(req.getStatus());
        if (req.getEventDate() != null) event.setEventDate(req.getEventDate());
        if (req.getParticipantsNumber() != null) event.setParticipantsNumber(req.getParticipantsNumber());
        if (req.getEstimatedTotalGrossRevenue() != null) event.setEstimatedTotalGrossRevenue(req.getEstimatedTotalGrossRevenue());

        if (req.getCompanyId() != null &&
                (event.getCompany() == null || !req.getCompanyId().equals(event.getCompany().getId()))) {
            Company company = companyRepository.findById(req.getCompanyId())
                    .orElseThrow(() -> new EntityNotFoundException("Company not found: " + req.getCompanyId()));
            event.setCompany(company);

            if (event.getContactPerson() != null &&
                    !event.getContactPerson().getCompany().getId().equals(company.getId())) {
                event.setContactPerson(null); // or company.getPrimaryContactPerson()
            }
        }

        if (req.getHotelId() != null &&
                (event.getHotel() == null || !req.getHotelId().equals(event.getHotel().getId()))) {
            Hotel hotel = hotelRepository.findById(req.getHotelId())
                    .orElseThrow(() -> new EntityNotFoundException("Hotel not found: " + req.getHotelId()));
            event.setHotel(hotel);
        }

        if (req.getContactPersonId() != null) {
            if (req.getContactPersonId() == 0) {
                event.setContactPerson(null);
            } else {
                Long companyId = (event.getCompany() != null) ? event.getCompany().getId() : req.getCompanyId();
                if (companyId == null) {
                    throw new IllegalArgumentException("Company must be set before assigning a contact person");
                }
                ContactPerson cp = contactPersonRepository
                        .findByIdAndCompanyId(req.getContactPersonId(), companyId)
                        .orElseThrow(() -> new IllegalArgumentException("Contact does not belong to the event's company"));
                event.setContactPerson(cp);
            }
        }

        event.setUpdatedAt(LocalDateTime.now());
        return eventRepository.save(event);
    }

    @Override
    public Long delete(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event with ID " + id + " not found"));
        eventRepository.delete(event);
        return id;
    }
}