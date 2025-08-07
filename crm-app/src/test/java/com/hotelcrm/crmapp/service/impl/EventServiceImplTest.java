package com.hotelcrm.crmapp.service.impl;

import com.hotelcrm.crmapp.dto.event.request.EventFilterRequest;
import com.hotelcrm.crmapp.entity.Event;
import com.hotelcrm.crmapp.entity.User;
import com.hotelcrm.crmapp.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateEventWithUserAndTimestamps() {
        User user = User.builder().id(1L).build();
        Event event = new Event();

        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Event savedEvent = eventService.create(event, user);

        assertEquals(user, savedEvent.getCreatedBy());
        assertNotNull(savedEvent.getCreatedAt());
        assertNotNull(savedEvent.getUpdatedAt());

        verify(eventRepository).save(event);
    }

    @Test
    void shouldReturnPagedEvents() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Event> page = new PageImpl<>(List.of(new Event()));

        when(eventRepository.findAll(pageable)).thenReturn(page);

        Page<Event> result = eventService.getEvents(pageable);

        assertEquals(1, result.getContent().size());
        verify(eventRepository).findAll(pageable);
    }

    @Test
    void shouldReturnEventById() {
        Event event = new Event();
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Optional<Event> result = eventService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(event, result.get());
    }

    @Test
    void shouldReturnFilteredEvents() {
        EventFilterRequest filter = new EventFilterRequest();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Event> page = new PageImpl<>(List.of(new Event()));

        when(eventRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);


        Page<Event> result = eventService.getFiltered(filter, pageable, 1L);

        assertEquals(1, result.getContent().size());
        verify(eventRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void shouldUpdateEventWithNewData() {
        Long eventId = 1L;
        Event existing = new Event();
        existing.setId(eventId);

        EventFilterRequest updateRequest = new EventFilterRequest();
        updateRequest.setName("New name");
        updateRequest.setDescription("Updated");
        updateRequest.setParticipantsNumber(100);
        updateRequest.setEstimatedTotalGrossRevenue(new BigDecimal("9999.99"));

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existing));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Event updated = eventService.update(updateRequest, eventId);

        assertEquals("New name", updated.getName());
        assertEquals("Updated", updated.getDescription());
        assertEquals(100, updated.getParticipantsNumber());
        assertEquals(new BigDecimal("9999.99"), updated.getEstimatedTotalGrossRevenue());

        verify(eventRepository).save(existing);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentEvent() {
        when(eventRepository.findById(404L)).thenReturn(Optional.empty());

        EventFilterRequest request = new EventFilterRequest();
        assertThrows(EntityNotFoundException.class, () -> eventService.update(request, 404L));
    }

    @Test
    void shouldDeleteExistingEvent() {
        Event event = new Event();
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Long result = eventService.delete(1L);

        assertEquals(1L, result);
        verify(eventRepository).delete(event);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentEvent() {
        when(eventRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> eventService.delete(404L));
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}