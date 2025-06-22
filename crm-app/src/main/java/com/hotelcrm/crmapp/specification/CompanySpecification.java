package com.hotelcrm.crmapp.specification;

import com.hotelcrm.crmapp.entity.Company;
import org.springframework.data.jpa.domain.Specification;

public class CompanySpecification {
    public static Specification<Company> hasName(String name) {
        return (root, query, cb)
                -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }


}
