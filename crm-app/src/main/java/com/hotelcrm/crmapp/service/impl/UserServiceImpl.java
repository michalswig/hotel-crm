package com.hotelcrm.crmapp.service.impl;

import com.hotelcrm.crmapp.exception.ConflictException;
import com.hotelcrm.crmapp.exception.NotFoundException;
import com.hotelcrm.crmapp.dto.user.request.UserRequest;
import com.hotelcrm.crmapp.entity.Hotel;
import com.hotelcrm.crmapp.entity.Role;
import com.hotelcrm.crmapp.entity.User;
import com.hotelcrm.crmapp.repository.HotelRepository;
import com.hotelcrm.crmapp.repository.RoleRepository;
import com.hotelcrm.crmapp.repository.UserRepository;
import com.hotelcrm.crmapp.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    @Transactional
    @Override
    public User create(UserRequest request) {

        String rawUsername = request.getUserName();
        if (rawUsername == null) {
            throw new IllegalArgumentException("Username must not be null");
        }
        String normalizedUserName = rawUsername.trim().toLowerCase();
        if (normalizedUserName.isEmpty()) {
            throw new IllegalArgumentException("Username must not be blank");
        }

        if (userRepository.existsByUsernameIgnoreCase(normalizedUserName)) {
            throw new ConflictException("Username already exists: " + request.getUserName());
        }

        Hotel hotel = hotelRepository.findById(request.getHotelId()).orElseThrow(
                () -> new NotFoundException("Hotel not found: " + request.getHotelId())
        );

        Role role = roleRepository.findById(request.getRoleId()).orElseThrow(
                () -> new NotFoundException("Role not found: " + request.getRoleId())
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

    @Transactional
    @Override
    public User update(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));

        String rawUsername = userRequest.getUserName();
        if (rawUsername == null) {
            throw new IllegalArgumentException("Username must not be null");
        }
        String normalizedUserName = rawUsername.trim().toLowerCase();
        if (normalizedUserName.isEmpty()) {
            throw new IllegalArgumentException("Username must not be blank");
        }
        if (userRepository.existsByUsernameIgnoreCaseAndIdNot(normalizedUserName, id)) {
            throw new ConflictException("Username already exists: " + userRequest.getUserName());
        }
        user.setUsername(normalizedUserName);

        // Password encoding
        String encodedPassword = passwordEncoder.encode(userRequest.getPassword());
        user.setPassword(encodedPassword);

        // Role and Hotel updates based on provided IDs
        Role role = roleRepository.findById(userRequest.getRoleId())
                .orElseThrow(() -> new NotFoundException("Role not found: " + userRequest.getRoleId()));
        Hotel hotel = hotelRepository.findById(userRequest.getHotelId())
                .orElseThrow(() -> new NotFoundException("Hotel not found: " + userRequest.getHotelId()));
        user.setRole(role);
        user.setHotel(hotel);

        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found: " + id));
        userRepository.delete(user);
    }


}