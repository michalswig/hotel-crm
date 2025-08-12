package com.hotelcrm.crmapp.service.impl;

import com.hotelcrm.crmapp.entity.User;
import com.hotelcrm.crmapp.repository.UserRepository;
import com.hotelcrm.crmapp.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }

}
