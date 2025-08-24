package com.hotelcrm.crmapp.specification;

import com.hotelcrm.crmapp.dto.event.request.EventFilterRequest;
import com.hotelcrm.crmapp.entity.Event;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class EventSpecification {

    public static Specification<Event> build(EventFilterRequest filter, Long userId) {
        Specification<Event> spec = Specification.where(createdBy(userId));

        if (filter.getId() != null) {
            spec = spec.and(eventIdEquals(filter.getId()));
        }
        if (filter.getName() != null) {
            spec = spec.and(nameLike(filter.getName()));
        }
        if (filter.getDescription() != null) {
            spec = spec.and(descriptionLike(filter.getDescription()));
        }
        if (filter.getType() != null) {
            spec = spec.and(typeEquals(filter.getType()));
        }
        if (filter.getStatus() != null) {
            spec = spec.and(statusEquals(filter.getStatus()));
        }
        if (filter.getEventDate() != null) {
            spec = spec.and(eventDateEquals(filter.getEventDate()));
        }
        if (filter.getParticipantsNumber() != null) {
            spec = spec.and(participantsNumberEquals(filter.getParticipantsNumber()));
        }
        if (filter.getEstimatedTotalGrossRevenue() != null) {
            spec = spec.and(revenueEquals(filter.getEstimatedTotalGrossRevenue()));
        }
        if (filter.getCompanyId() != null) {
            spec = spec.and(companyEquals(filter.getCompanyId()));
        }
        if (filter.getHotelId() != null) {
            spec = spec.and(hotelEquals(filter.getHotelId()));
        }

        return spec;
    }

    private static Specification<Event> createdBy(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("createdBy").get("id"), userId);
    }

    private static Specification<Event> eventIdEquals(Long id) {
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }

    private static Specification<Event> nameLike(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    private static Specification<Event> descriptionLike(String description) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }

    private static Specification<Event> typeEquals(Enum<?> type) {
        return (root, query, cb) -> cb.equal(root.get("type"), type);
    }

    private static Specification<Event> statusEquals(Enum<?> status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    private static Specification<Event> eventDateEquals(LocalDateTime date) {
        return (root, query, cb) -> cb.equal(root.get("eventDate"), date);
    }

    private static Specification<Event> participantsNumberEquals(Integer number) {
        return (root, query, cb) -> cb.equal(root.get("participantsNumber"), number);
    }

    private static Specification<Event> revenueEquals(java.math.BigDecimal revenue) {
        return (root, query, cb) -> cb.equal(root.get("estimatedTotalGrossRevenue"), revenue);
    }

    private static Specification<Event> companyEquals(Long companyId) {
        return (root, query, cb) -> cb.equal(root.get("company").get("id"), companyId);
    }

    private static Specification<Event> hotelEquals(Long hotelId) {
        return (root, query, cb) -> cb.equal(root.get("hotel").get("id"), hotelId);
    }
}
