package com.hotelcrm.crmapp.service;

import com.hotelcrm.crmapp.dto.contactperson.request.ContactPersonRequest;
import com.hotelcrm.crmapp.dto.contactperson.response.ContactPersonResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactPersonService {
    Page<ContactPersonResponse> list(Long companyId, Pageable pageable);
    ContactPersonResponse create(Long companyId, ContactPersonRequest request);
    ContactPersonResponse update(Long companyId, Long contactId, ContactPersonRequest request);
    void delete(Long companyId, Long contactId);
}
