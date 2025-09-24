package com.hotelcrm.crmapp.specification;

import com.hotelcrm.crmapp.dto.interaction.request.InteractionFilterRequest;
import com.hotelcrm.crmapp.entity.Interaction;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class InteractionSpecification {

    public static Specification<Interaction> build(InteractionFilterRequest f, Long userId) {
        Specification<Interaction> spec = createdBy(userId);

        if (f == null) return spec;

        if (f.getId() != null)               spec = spec.and(eqId(f.getId()));
        if (f.getType() != null)             spec = spec.and(eqType(f.getType()));
        if (f.getCompanyId() != null)        spec = spec.and(eqCompany(f.getCompanyId()));
        if (f.getContactPersonId() != null)  spec = spec.and(eqContact(f.getContactPersonId()));

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

    private static Specification<Interaction> eqCompany(Long companyId) {
        return (r, q, cb) -> cb.equal(r.get("company").get("id"), companyId);
    }

    private static Specification<Interaction> eqContact(Long contactId) {
        return (r, q, cb) -> cb.equal(r.get("contactPerson").get("id"), contactId);
    }

    private static Specification<Interaction> scheduledAtGte(LocalDateTime t) {
        return (r, q, cb) -> cb.greaterThanOrEqualTo(r.get("scheduledAt"), t);
    }

    private static Specification<Interaction> scheduledAtLte(LocalDateTime t) {
        return (r, q, cb) -> cb.lessThanOrEqualTo(r.get("scheduledAt"), t);
    }

    private static Specification<Interaction> completedAtGte(LocalDateTime t) {
        return (r, q, cb) -> cb.greaterThanOrEqualTo(r.get("completedAt"), t);
    }

    private static Specification<Interaction> completedAtLte(LocalDateTime t) {
        return (r, q, cb) -> cb.lessThanOrEqualTo(r.get("completedAt"), t);
    }

    private static Specification<Interaction> followUpAtGte(LocalDateTime t) {
        return (r, q, cb) -> cb.greaterThanOrEqualTo(r.get("followUpAt"), t);
    }

    private static Specification<Interaction> followUpAtLte(LocalDateTime t) {
        return (r, q, cb) -> cb.lessThanOrEqualTo(r.get("followUpAt"), t);
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
}
