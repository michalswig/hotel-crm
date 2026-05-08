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

    @Operation(summary = "Create a new event")
    @ApiResponse(responseCode = "200", description = "Event successfully created")
    @PostMapping
    @PreAuthorize("hasAnyRole('SPECIALIST', 'MANAGER', 'ADMINISTRATOR')")
    public EventDetailResponse create(
            @Valid @RequestBody EventCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Event created = eventService.create(request, userDetails.getUser());
        return EventMapper.toDetailResponse(created);
    }

    @Operation(summary = "Get paginated and optionally filtered list of events")
    @ApiResponse(responseCode = "200", description = "Events successfully fetched")
    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('SPECIALIST', 'MANAGER', 'ADMINISTRATOR')")
    public Page<EventDetailResponse> getFiltered(
            @ParameterObject EventFilterRequest filter,
            @ParameterObject Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return eventService.getFiltered(filter, pageable, userDetails.getUser().getId())
                .map(EventMapper::toDetailResponse);
    }

    @Operation(summary = "Get event by ID")
    @ApiResponse(responseCode = "200", description = "Event found")
    @ApiResponse(responseCode = "404", description = "Event not found")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SPECIALIST', 'MANAGER', 'ADMINISTRATOR')")
    public EventDetailResponse getById(@PathVariable Long id) {
        return eventService.findById(id)
                .map(EventMapper::toDetailResponse)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
    }

    @Operation(summary = "Update an event")
    @ApiResponse(responseCode = "200", description = "Event successfully updated")
    @ApiResponse(responseCode = "403", description = "Not allowed to modify this event")
    @ApiResponse(responseCode = "404", description = "Event not found")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SPECIALIST', 'MANAGER', 'ADMINISTRATOR')")
    public EventDetailResponse update(
            @PathVariable Long id,
            @Valid @RequestBody EventUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Event updated = eventService.update(id, request, userDetails.getUser());
        return EventMapper.toDetailResponse(updated);
    }

    @Operation(summary = "Delete an event")
    @ApiResponse(responseCode = "200", description = "Event successfully deleted")
    @ApiResponse(responseCode = "403", description = "Not allowed to delete this event")
    @ApiResponse(responseCode = "404", description = "Event not found")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SPECIALIST', 'MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<Long> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(eventService.delete(id, userDetails.getUser()));
    }
}