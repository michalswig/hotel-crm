package com.hotelcrm.crmapp.controller;

import com.hotelcrm.crmapp.dto.user.UserResponse;
import com.hotelcrm.crmapp.entity.User;
import com.hotelcrm.crmapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hotelcrm.crmapp.dto.user.UserMapper.toResponse;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user by ID", description = "Returns details of a specific user by ID")
    @ApiResponse(responseCode = "200", description = "User successfully retrieved")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "ID of the user to retrieve") @PathVariable Long id) {

        User user = userService.getById(id);
        return ResponseEntity.ok(toResponse(user));
    }
}
