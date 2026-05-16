package com.hotelcrm.crmapp.specification;

import com.hotelcrm.crmapp.dto.company.CompanyFilter;
import com.hotelcrm.crmapp.entity.Company;
import com.hotelcrm.crmapp.enums.Industry;
import org.springframework.data.jpa.domain.Specification;

public class CompanySpecification {
    public static Specification<Company> build(CompanyFilter filter, Long userId) {
        Specification<Company> spec = Specification.where(createdBy(userId));

        if (filter.getId() != null) {
            spec = spec.and(hasId(filter.getId()));
        }
        if (filter.getName() != null && !filter.getName().isBlank()) {
            spec = spec.and(nameLike(filter.getName()));
        }
        if (filter.getTaxId() != null && !filter.getTaxId().isBlank()) {
            spec = spec.and(taxIdEquals(filter.getTaxId()));
        }
        if (filter.getIndustry() != null) {
            spec = spec.and(industryEquals(filter.getIndustry()));
        }
        if (filter.getEmail() != null && !filter.getEmail().isBlank()) {
            spec = spec.and(emailEquals(filter.getEmail()));
        }
        if (filter.getPhoneNumber() != null && !filter.getPhoneNumber().isBlank()) {
            spec = spec.and(phoneNumberEquals(filter.getPhoneNumber()));
        }
        if (filter.getWebsite() != null && !filter.getWebsite().isBlank()) {
            spec = spec.and(websiteEquals(filter.getWebsite()));
        }
        if (filter.getAddress() != null && !filter.getAddress().isBlank()) {
            spec = spec.and(addressLike(filter.getAddress()));
        }
        if (filter.getPostalCode() != null && !filter.getPostalCode().isBlank()) {
            spec = spec.and(postalCodeEquals(filter.getPostalCode()));
        }
        if (filter.getCity() != null && !filter.getCity().isBlank()) {
            spec = spec.and(cityEquals(filter.getCity()));
        }
        if (filter.getCountry() != null && !filter.getCountry().isBlank()) {
            spec = spec.and(countryEquals(filter.getCountry()));
        }

        return spec;
    }

    private static Specification<Company> createdBy(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("createdBy").get("id"), userId);
    }

    private static Specification<Company> hasId(Long id) {
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }

    private static Specification<Company> nameLike(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    private static Specification<Company> taxIdEquals(String taxId) {
        return (root, query, cb) -> cb.equal(root.get("taxId"), taxId);
    }

    private static Specification<Company> industryEquals(Industry industry) {
        return (root, query, cb) -> cb.equal(root.get("industry"), industry);
    }

    private static Specification<Company> emailEquals(String email) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get("email")), email.toLowerCase());
    }

    private static Specification<Company> phoneNumberEquals(String phoneNumber) {
        return (root, query, cb) -> cb.equal(root.get("phoneNumber"), phoneNumber);
    }

    private static Specification<Company> websiteEquals(String website) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get("website")), website.toLowerCase());
    }

    private static Specification<Company> addressLike(String address) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("address")), "%" + address.toLowerCase() + "%");
    }

    private static Specification<Company> postalCodeEquals(String postalCode) {
        return (root, query, cb) -> cb.equal(root.get("postalCode"), postalCode);
    }

    private static Specification<Company> cityEquals(String city) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("city")), "%" + city.toLowerCase() + "%");
    }

    private static Specification<Company> countryEquals(String country) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get("country")), country.toLowerCase());
    }


}
