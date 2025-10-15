package com.hotelcrm.crmapp.controller;

import com.hotelcrm.crmapp.dto.contactperson.request.ContactPersonRequest;
import com.hotelcrm.crmapp.dto.contactperson.response.ContactPersonResponse;
import com.hotelcrm.crmapp.service.ContactPersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/companies/{companyId}/contacts")
@RequiredArgsConstructor
public class ContactPersonController {

    private final ContactPersonService contactService;

    @GetMapping
    public ResponseEntity<Page<ContactPersonResponse>> list(
            @PathVariable Long companyId,
            @PageableDefault(size = 10, sort = "lastName") Pageable pageable) {
        return ResponseEntity.ok(contactService.list(companyId, pageable));
    }

    @PreAuthorize("hasAnyRole('MANAGER','SPECIALIST','ADMINISTRATOR')")
    @PostMapping
    public ResponseEntity<ContactPersonResponse> create(
            @PathVariable Long companyId,
            @Valid @RequestBody ContactPersonRequest req) {
        return ResponseEntity.ok(contactService.create(companyId, req));
    }

    @PreAuthorize("hasAnyRole('MANAGER','SPECIALIST','ADMINISTRATOR')")
    @PutMapping("/{contactId}")
    public ResponseEntity<ContactPersonResponse> update(
            @PathVariable Long companyId,
            @PathVariable Long contactId,
            @Valid @RequestBody ContactPersonRequest req) {
        return ResponseEntity.ok(contactService.update(companyId, contactId, req));
    }

    @PreAuthorize("hasAnyRole('MANAGER','SPECIALIST','ADMINISTRATOR')")
    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long companyId,
            @PathVariable Long contactId) {
        contactService.delete(companyId, contactId);
        return ResponseEntity.noContent().build();
    }
}
