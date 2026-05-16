package com.hotelcrm.crmapp.specification;

import com.hotelcrm.crmapp.entity.User;
import com.hotelcrm.crmapp.enums.RoleType;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> hasUsername(String username) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%");
    }

    public static Specification<User> hasRole(String roleName) {
        return (root, query, cb) -> cb.equal(root.get("role").get("name"), RoleType.valueOf(roleName));
    }
}
