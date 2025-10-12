package com.hotelcrm.crmapp.service.impl;

import com.github.dockerjava.api.exception.ConflictException;
import com.hotelcrm.crmapp.dto.user.request.UserRequest;
import com.hotelcrm.crmapp.entity.Hotel;
import com.hotelcrm.crmapp.entity.Role;
import com.hotelcrm.crmapp.entity.User;
import com.hotelcrm.crmapp.repository.HotelRepository;
import com.hotelcrm.crmapp.repository.RoleRepository;
import com.hotelcrm.crmapp.repository.UserRepository;
import com.hotelcrm.crmapp.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }

    @Transactional
    @Override
    public User create(UserRequest request) {

        String normalizedUserName = request.getUserName().trim().toLowerCase();

        if (userRepository.existsByUsernameIgnoreCase(normalizedUserName)) {
            throw new ConflictException("Username already exists: " + request.getUserName());
        }

        Hotel hotel = hotelRepository.findById(request.getHotelId()).orElseThrow(
                () -> new EntityNotFoundException("Hotel not found: " + request.getHotelId())
        );

        Role role = roleRepository.findById(request.getRoleId()).orElseThrow(
                () -> new EntityNotFoundException("Role not found: " + request.getRoleId())
        );

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .username(normalizedUserName)
                .password(encodedPassword)
                .hotel(hotel)
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }




}