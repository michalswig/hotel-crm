package com.hotelcrm.crmapp.service;

import com.hotelcrm.crmapp.dto.event.request.EventFilterRequest;
import com.hotelcrm.crmapp.entity.Event;
import com.hotelcrm.crmapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EventService {
    Event create(Event event, User user);
    Page<Event> getEvents(Pageable pageable);
    Optional<Event> findById(Long id);
    Page<Event> getFiltered(EventFilterRequest filter, Pageable pageable, Long userId);
    Event update(EventFilterRequest request, Long id);
    Long delete(Long id);
}
