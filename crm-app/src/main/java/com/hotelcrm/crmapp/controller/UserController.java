package com.hotelcrm.crmapp.controller;

import com.hotelcrm.crmapp.dto.UserDto;
import com.hotelcrm.crmapp.entity.User;
import com.hotelcrm.crmapp.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @GetMapping
    public ResponseEntity<Page<UserDto>> getUsers(Pageable pageable) {
        Page<UserDto> userPage = userServiceImpl.getUsers(pageable)
                .map(UserDto::fromEntity);
        return ResponseEntity.ok(userPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        User user = userServiceImpl.getById(id);
        return ResponseEntity.ok(UserDto.fromEntity(user));
    }


}
