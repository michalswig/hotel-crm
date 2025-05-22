package com.hotelcrm.crmapp.controller;

import com.hotelcrm.crmapp.dto.UserDto;
import com.hotelcrm.crmapp.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userServiceImpl.getAllUsers().stream()
                .map(UserDto::fromEntity)
                .toList();
    }

}
