package com.hotelcrm.crmapp.service.impl;

import com.hotelcrm.crmapp.dto.event.request.EventFilterRequest;
import com.hotelcrm.crmapp.entity.Event;
import com.hotelcrm.crmapp.entity.User;
import com.hotelcrm.crmapp.repository.EventRepository;
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

    @Override
    public Event create(Event event, User user) {
        event.setCreatedBy(user);
        event.setCreatedAt(LocalDateTime.now());
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
    public Long delete(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event with ID " + id + " not found"));

        eventRepository.delete(event);
        return id;
    }
}
