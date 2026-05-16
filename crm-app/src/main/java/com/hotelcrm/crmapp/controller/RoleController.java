package com.hotelcrm.crmapp.controller;

import com.hotelcrm.crmapp.entity.Role;
import com.hotelcrm.crmapp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleRepository roleRepository;

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping
    public List<Role> getAll() {
        return roleRepository.findAll();
    }
}
