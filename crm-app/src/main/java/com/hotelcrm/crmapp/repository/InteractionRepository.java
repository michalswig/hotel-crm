package com.hotelcrm.crmapp.repository;

import com.hotelcrm.crmapp.entity.Interaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface InteractionRepository extends JpaRepository<Interaction, Long>, JpaSpecificationExecutor<Interaction> {

    Page<Interaction> findByUser_IdAndScheduledAtBetween(Long userId, LocalDate from, LocalDate to, Pageable pageable);

    List<Interaction> findTop50ByUser_IdAndFollowUpAtAfterOrderByFollowUpAtAsc(Long userId, LocalDate now);
}