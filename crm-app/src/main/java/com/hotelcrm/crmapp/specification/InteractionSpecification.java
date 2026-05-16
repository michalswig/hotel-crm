package com.hotelcrm.crmapp.specification;

import com.hotelcrm.crmapp.dto.interaction.request.InteractionFilterRequest;
import com.hotelcrm.crmapp.entity.Interaction;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;

public class InteractionSpecification {

    public static Specification<Interaction> build(InteractionFilterRequest f, Long userId) {
        Specification<Interaction> spec = createdBy(userId);

        if (f == null) return spec;

        if (f.getId() != null)               spec = spec.and(eqId(f.getId()));
        if (f.getType() != null)             spec = spec.and(eqType(f.getType()));
        if (f.getStatus() != null)           spec = spec.and(eqStatus(f.getStatus()));
        if (f.getCompanyId() != null)        spec = spec.and(eqCompany(f.getCompanyId()));
        if (f.getContactPersonId() != null)  spec = spec.and(eqContact(f.getContactPersonId()));
        if (f.getNotes() != null && !f.getNotes().isBlank()) spec = spec.and(notesContains(f.getNotes()));
        if (f.getQ() != null && !f.getQ().isBlank())        spec = spec.and(quick(f.getQ()));

        if (f.getScheduledFrom() != null)    spec = spec.and(scheduledAtGte(f.getScheduledFrom()));
        if (f.getScheduledTo() != null)      spec = spec.and(scheduledAtLte(f.getScheduledTo()));

        if (f.getCompletedFrom() != null)    spec = spec.and(completedAtGte(f.getCompletedFrom()));
        if (f.getCompletedTo() != null)      spec = spec.and(completedAtLte(f.getCompletedTo()));

        if (f.getFollowUpFrom() != null)     spec = spec.and(followUpAtGte(f.getFollowUpFrom()));
        if (f.getFollowUpTo() != null)       spec = spec.and(followUpAtLte(f.getFollowUpTo()));

        if (Boolean.TRUE.equals(f.getPendingOnly()))     spec = spec.and(completedIsNull());
        if (Boolean.TRUE.equals(f.getCompletedOnly()))   spec = spec.and(completedIsNotNull());
        if (Boolean.TRUE.equals(f.getWithFollowUpOnly())) spec = spec.and(followUpIsNotNull());

        return spec;
    }

    public static Specification<Interaction> scheduledBetween(LocalDateTime from, LocalDateTime to) {
        Specification<Interaction> spec = Specification.where((root, q, cb) -> cb.conjunction());
        if (from != null) spec = spec.and(scheduledAtGte(from));
        if (to != null)   spec = spec.and(scheduledAtLte(to));
        return spec;
    }

    public static Specification<Interaction> createdBy(Long userId) {
        return (r, q, cb) -> cb.equal(r.get("user").get("id"), userId);
    }

    private static Specification<Interaction> eqId(Long id) {
        return (r, q, cb) -> cb.equal(r.get("id"), id);
    }

    private static Specification<Interaction> eqType(Enum<?> type) {
        return (r, q, cb) -> cb.equal(r.get("type"), type);
    }

    private static Specification<Interaction> eqStatus(Enum<?> status) {
        return (r, q, cb) -> cb.equal(r.get("status"), status);
    }

    private static Specification<Interaction> notesContains(String s) {
        return (r, q, cb) -> cb.like(cb.lower(r.get("notes")), "%" + s.toLowerCase() + "%");
    }

    private static Specification<Interaction> eqCompany(Long companyId) {
        return (r, q, cb) -> cb.equal(r.get("company").get("id"), companyId);
    }

    private static Specification<Interaction> eqContact(Long contactId) {
        return (r, q, cb) -> cb.equal(r.get("contactPerson").get("id"), contactId);
    }

    private static Specification<Interaction> scheduledAtGte(LocalDateTime d) {
        return (r, q, cb) -> cb.greaterThanOrEqualTo(r.get("scheduledAt").as(LocalDateTime.class), d);
    }

    private static Specification<Interaction> scheduledAtLte(LocalDateTime d) {
        return (r, q, cb) -> cb.lessThanOrEqualTo(r.get("scheduledAt").as(LocalDateTime.class), d);
    }

    private static Specification<Interaction> completedAtGte(LocalDateTime t) {
        return (r, q, cb) -> cb.greaterThanOrEqualTo(r.get("completedAt").as(LocalDateTime.class), t);
    }

    private static Specification<Interaction> completedAtLte(LocalDateTime t) {
        return (r, q, cb) -> cb.lessThanOrEqualTo(r.get("completedAt").as(LocalDateTime.class), t);
    }

    private static Specification<Interaction> followUpAtGte(LocalDateTime d) {
        return (r, q, cb) -> cb.greaterThanOrEqualTo(r.get("followUpAt").as(LocalDateTime.class), d);
    }

    private static Specification<Interaction> followUpAtLte(LocalDateTime d) {
        return (r, q, cb) -> cb.lessThanOrEqualTo(r.get("followUpAt").as(LocalDateTime.class), d);
    }

    private static Specification<Interaction> completedIsNull() {
        return (r, q, cb) -> cb.isNull(r.get("completedAt"));
    }

    private static Specification<Interaction> completedIsNotNull() {
        return (r, q, cb) -> cb.isNotNull(r.get("completedAt"));
    }

    private static Specification<Interaction> followUpIsNotNull() {
        return (r, q, cb) -> cb.isNotNull(r.get("followUpAt"));
    }

    private static Specification<Interaction> quick(String query) {
        return (r, q, cb) -> {
            String s = "%" + query.toLowerCase() + "%";
            var preds = new ArrayList<Predicate>();
            preds.add(cb.like(cb.lower(r.get("notes")), s));
            preds.add(cb.like(cb.lower(r.get("company").get("name")), s));
            preds.add(cb.like(cb.lower(r.get("contactPerson").get("firstName")), s));
            preds.add(cb.like(cb.lower(r.get("contactPerson").get("lastName")), s));
            preds.add(cb.like(cb.lower(cb.concat(cb.concat(r.get("contactPerson").get("firstName"), " "), r.get("contactPerson").get("lastName"))), s));
            preds.add(cb.like(cb.lower(r.get("type").as(String.class)), s));
            preds.add(cb.like(cb.lower(r.get("status").as(String.class)), s));
            return cb.or(preds.toArray(new Predicate[0]));
        };
    }
}
