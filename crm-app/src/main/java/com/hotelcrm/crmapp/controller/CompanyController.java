package com.hotelcrm.crmapp.controller;

import com.hotelcrm.crmapp.config.CustomUserDetails;
import com.hotelcrm.crmapp.dto.company.*;
import com.hotelcrm.crmapp.entity.Company;
import com.hotelcrm.crmapp.mapper.CompanyMapper;
import com.hotelcrm.crmapp.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @Operation(
            summary = "Create a new company",
            description = "Creates a company using the request data")
    @ApiResponse(responseCode = "200", description = "Company successfully created")
    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(
            @Valid @RequestBody CompanyRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Company created = companyService.createCompany(request, userDetails.getUser());
        return ResponseEntity.ok(CompanyMapper.toResponse(created));
    }


    @Operation(
            summary = "Update company",
            description = "Replaces all editable fields of an existing company")
    @ApiResponse(responseCode = "200", description = "Company successfully updated")
    @ApiResponse(responseCode = "404", description = "Company not found")
    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(
            @PathVariable Long id,
            @Valid @RequestBody CompanyFilter filter) {
        Company updated = companyService.updateCompany(id, filter);
        return ResponseEntity.ok(CompanyMapper.toResponse(updated));
    }

    @Operation(
            summary = "Delete company",
            description = "Removes a company by ID")
    @ApiResponse(responseCode = "204", description = "Company deleted")
    @ApiResponse(responseCode = "404", description = "Company not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get paginated list of companies",
            description = "Returns a page of companies with pagination and sorting")
    @ApiResponse(responseCode = "200", description = "List of companies successfully retrieved")
    @GetMapping
    public ResponseEntity<Page<CompanyResponse>> getCompanies(
            @Parameter(description = "Pagination and sorting options")
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {

        Page<CompanyResponse> companies = companyService.getCompanies(pageable)
                .map(CompanyMapper::toResponse);
        return ResponseEntity.ok(companies);
    }

    @Operation(
            summary = "Get company by ID",
            description = "Returns company details by ID")
    @ApiResponse(responseCode = "200", description = "Company successfully retrieved")
    @ApiResponse(responseCode = "404", description = "Company not found")
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long id) {
        Company company = companyService.getById(id);
        return ResponseEntity.ok(CompanyMapper.toDetailResponse(company));
    }

    @Operation(
            summary = "Filter *my* companies",
            description = "Returns a paginated list of companies created by the current user; "
                    + "additional filters (name, city, industry, …) are optional query params")
    @ApiResponse(responseCode = "200", description = "Filtered list successfully retrieved")
    @GetMapping("/filter")
    public ResponseEntity<Page<CompanyResponse>> getFilteredCompaniesCreatedByCurrentUser(
            @ModelAttribute CompanyFilter filter,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {

        Page<CompanyResponse> page = companyService
                .getFilteredCompanies(filter, currentUser.getId(), pageable)
                .map(CompanyMapper::toResponse);

        return ResponseEntity.ok(page);
    }

    @ApiResponse(
            responseCode = "200",
            description = "Overview generated")
    @GetMapping("/summary")
    public ResponseEntity<List<CompanySummaryDto>> getCompanySummaryTable(
            @RequestParam(required = false) Integer ytdYear,
            @RequestParam(required = false) Integer lyYear) {

        int currentYear = java.time.Year.now().getValue();
        int yearYTD = (ytdYear != null ? ytdYear : currentYear);
        int yearLY = (lyYear != null ? lyYear : currentYear - 1);

        List<CompanySummaryDto> summary = companyService.fetchCompanySummaryTable(yearYTD, yearLY);
        return ResponseEntity.ok(summary);
    }

    @Operation(summary = "Set primary contact for a company")
    @ApiResponse(responseCode = "204", description = "Primary contact set")
    @PatchMapping("/{id}/primary-contact")
    public ResponseEntity<Void> setPrimaryContact(
            @PathVariable Long id,
            @RequestBody SetPrimaryContactRequest req) {
        companyService.setPrimaryContact(id, req.contactId());
        return ResponseEntity.noContent().build();
    }

}
