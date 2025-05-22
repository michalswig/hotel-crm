package com.hotelcrm.crmapp.service.impl;

import com.hotelcrm.crmapp.entity.User;
import com.hotelcrm.crmapp.repository.UserRepository;
import com.hotelcrm.crmapp.service.UserService;
import com.hotelcrm.crmapp.specification.UserSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public Page<User> filterUsers(String username, String role, Pageable pageable) {
        Specification<User> spec = Specification.where(null);

        if (username != null) {
            spec = spec.and(UserSpecification.hasUsername(username));
        }
        if (role != null) {
            spec = spec.and(UserSpecification.hasRole(role));
        }

        return userRepository.findAll(spec, pageable);
    }


}
