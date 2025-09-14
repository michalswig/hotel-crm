package com.hotelcrm.crmapp.repository;

import com.hotelcrm.crmapp.entity.ContactPerson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactPersonRepository extends JpaRepository<ContactPerson, Long> {
    boolean existsByCompanyIdAndEmailIgnoreCase(Long companyId, String email);
    Page<ContactPerson> findByCompanyId(Long companyId, Pageable pageable);
    Optional<ContactPerson> findByIdAndCompanyId(Long id, Long companyId);
}
