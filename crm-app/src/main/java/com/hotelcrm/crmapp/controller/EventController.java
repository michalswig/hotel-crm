package com.hotelcrm.crmapp.controller;

import com.hotelcrm.crmapp.config.CustomUserDetails;
import com.hotelcrm.crmapp.dto.event.mapper.EventMapper;
import com.hotelcrm.crmapp.dto.event.request.EventCreateRequest;
import com.hotelcrm.crmapp.dto.event.request.EventFilterRequest;
import com.hotelcrm.crmapp.dto.event.request.EventUpdateRequest;
import com.hotelcrm.crmapp.dto.event.response.EventDetailResponse;
import com.hotelcrm.crmapp.entity.Event;
import com.hotelcrm.crmapp.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @Operation(
            summary = "Create a new event",
            description = "Creates an event assigned to the logged-in user's hotel using the provided data"
    )
    @ApiResponse(responseCode = "200", description = "Event successfully created")
    @PostMapping
    @PreAuthorize("hasAnyRole('SPECIALIST', 'MANAGER')")
    public EventDetailResponse create(
            @Valid @RequestBody EventCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // Authentication handled by Spring Security

        Event created = eventService.create(request, userDetails.getUser());
        return EventMapper.toDetailResponse(created);
    }

    @Operation(
            summary = "Get paginated and optionally filtered list of events",
            description = "Returns a page of events filtered by request parameters and created by the logged-in user"
    )
    @ApiResponse(responseCode = "200", description = "Events successfully fetched")
    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('SPECIALIST', 'MANAGER')")
    public Page<EventDetailResponse> getFiltered(
            @ParameterObject EventFilterRequest filter,
            @ParameterObject Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // Authentication handled by Spring Security

        return eventService.getFiltered(filter, pageable, userDetails.getUser().getId())
                .map(EventMapper::toDetailResponse);
    }

    @Operation(
            summary = "Get event by ID",
            description = "Returns a single event by its ID"
    )
    @ApiResponse(responseCode = "200", description = "Event found")
    @ApiResponse(responseCode = "404", description = "Event not found")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SPECIALIST', 'MANAGER')")
    public EventDetailResponse getById(@PathVariable Long id) {
        return eventService.findById(id)
                .map(EventMapper::toDetailResponse)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
    }

    @Operation(
            summary = "Update an event",
            description = "Updates an existing event by ID with the provided data"
    )
    @ApiResponse(responseCode = "200", description = "Event successfully updated")
    @ApiResponse(responseCode = "404", description = "Event not found")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SPECIALIST', 'MANAGER')")
    public EventDetailResponse update(
            @PathVariable Long id,
            @Valid @RequestBody EventUpdateRequest request) {

        Event updated = eventService.update(id, request);
        return EventMapper.toDetailResponse(updated);
    }

    @Operation(
            summary = "Delete an event",
            description = "Deletes an event by ID"
    )
    @ApiResponse(responseCode = "200", description = "Event successfully deleted")
    @ApiResponse(responseCode = "404", description = "Event not found")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SPECIALIST', 'MANAGER')")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.delete(id));
    }
}