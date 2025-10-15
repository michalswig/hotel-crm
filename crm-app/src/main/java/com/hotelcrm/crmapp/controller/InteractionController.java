package com.hotelcrm.crmapp.controller;

import com.hotelcrm.crmapp.config.CustomUserDetails;
import com.hotelcrm.crmapp.dto.interaction.request.InteractionCompleteRequest;
import com.hotelcrm.crmapp.dto.interaction.request.InteractionCreateRequest;
import com.hotelcrm.crmapp.dto.interaction.request.InteractionFilterRequest;
import com.hotelcrm.crmapp.dto.interaction.request.InteractionUpdateRequest;
import com.hotelcrm.crmapp.dto.interaction.response.InteractionResponse;
import com.hotelcrm.crmapp.service.InteractionService;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/interactions")
@RequiredArgsConstructor
public class InteractionController {

    private final InteractionService interactionService;

    @Operation(
            summary = "Create interaction",
            description = "Schedules a new interaction (future date). Returns 201 with Location header.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Interaction created"),
                    @ApiResponse(responseCode = "400", description = "Validation error"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PreAuthorize("hasAnyRole('SPECIALIST','MANAGER','ADMINISTRATOR')")
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<InteractionResponse> create(
            @Valid @RequestBody InteractionCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // Authentication handled by Spring Security

        InteractionResponse created = interactionService.create(request, userDetails.getUser());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Filter my interactions",
            description = "Returns a page of interactions filtered by params; only the current user's interactions.")
    @ApiResponse(responseCode = "200", description = "Interactions fetched")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/filter")
    public Page<InteractionResponse> getFiltered(
            @ParameterObject InteractionFilterRequest filter,
            @ParameterObject Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // Authentication handled by Spring Security
        return interactionService.getFiltered(filter, pageable, userDetails.getUser().getId());
    }

    @Operation(summary = "Get interaction by ID",
            description = "Returns a single interaction if it belongs to the current user.")
    @ApiResponse(responseCode = "200", description = "Interaction found")
    @ApiResponse(responseCode = "404", description = "Interaction not found")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public InteractionResponse getById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // Authentication handled by Spring Security
        return interactionService.getById(id, userDetails.getUser())
                .orElseThrow(() -> new EntityNotFoundException("Interaction not found"));
    }

    @Operation(summary = "Update interaction",
            description = "Updates fields like type/notes/scheduledAt/contact (only if not completed).")
    @ApiResponse(responseCode = "200", description = "Interaction updated")
    @PreAuthorize("hasAnyRole('SPECIALIST','MANAGER','ADMINISTRATOR')")
    @PutMapping("/{id}")
    public InteractionResponse update(
            @PathVariable Long id,
            @Valid @RequestBody InteractionUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // Authentication handled by Spring Security
        return interactionService.update(id, request, userDetails.getUser());
    }

    @Operation(summary = "Complete interaction",
            description = "Marks an interaction as completed, attaches notes, and sets an optional follow-up time.")
    @ApiResponse(responseCode = "200", description = "Interaction completed")
    @PreAuthorize("hasAnyRole('SPECIALIST','MANAGER','ADMINISTRATOR')")
    @PatchMapping("/{id}/complete")
    public InteractionResponse complete(
            @PathVariable Long id,
            @Valid @RequestBody InteractionCompleteRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // Authentication handled by Spring Security
        return interactionService.complete(id, request, userDetails.getUser());
    }

    @Operation(summary = "Calendar",
            description = "Returns interactions scheduled between 'from' and 'to' for the current user.")
    @ApiResponse(responseCode = "200", description = "Calendar page fetched")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/calendar")
    public Page<InteractionResponse> calendar(
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to,
            @ParameterObject Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        // Authentication handled by Spring Security
        return interactionService.calendar(userDetails.getUser().getId(), from, to, pageable);
    }

    @Operation(summary = "Upcoming follow-ups",
            description = "Returns up to 50 upcoming follow-ups for the current user, soonest first.")
    @ApiResponse(responseCode = "200", description = "Follow-ups fetched")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/follow-ups")
    public List<InteractionResponse> upcomingFollowUps(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        // Authentication handled by Spring Security
        return interactionService.upcomingFollowUps(userDetails.getUser().getId());
    }

    @Operation(summary = "Delete interaction", description = "Deletes an interaction owned by the current user.")
    @ApiResponse(responseCode = "204", description = "Interaction deleted")
    @PreAuthorize("hasAnyRole('SPECIALIST','MANAGER','ADMINISTRATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // Authentication handled by Spring Security
        interactionService.delete(id, userDetails.getUser());
        return ResponseEntity.noContent().build();
    }
}
