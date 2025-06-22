package com.hotelcrm.crmapp.controller;

import com.hotelcrm.crmapp.dto.UserDto;
import com.hotelcrm.crmapp.entity.User;
import com.hotelcrm.crmapp.mapper.UserMapper;
import com.hotelcrm.crmapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Get paginated list of users", description = "Returns a page of users with pagination and sorting")
    @ApiResponse(responseCode = "200", description = "List of users successfully retrieved")
    @GetMapping
    public ResponseEntity<Page<UserDto>> getUsers(
            @Parameter(description = "Pagination and sorting options")
            @PageableDefault(size = 10, sort = "username") Pageable pageable) {

        Page<UserDto> userPage = userService.getUsers(pageable)
                .map(userMapper::toDto);
        return ResponseEntity.ok(userPage);
    }

    @Operation(summary = "Get user by ID", description = "Returns details of a specific user by ID")
    @ApiResponse(responseCode = "200", description = "User successfully retrieved")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "ID of the user to retrieve") @PathVariable Long id) {

        User user = userService.getById(id);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @Operation(summary = "Filter users by username and/or role", description = "Returns a paginated list of users filtered by optional username and role")
    @ApiResponse(responseCode = "200", description = "Filtered list of users successfully retrieved")
    @GetMapping("/filter")
    public ResponseEntity<Page<UserDto>> getFilteredUsers(
            @Parameter(description = "Username to filter by") @RequestParam(required = false) String username,
            @Parameter(description = "Role to filter by (e.g. ADMINISTRATOR, MANAGER)") @RequestParam(required = false) String role,
            @Parameter(description = "Pagination and sorting options")
            @PageableDefault(size = 10, sort = "username") Pageable pageable) {

        Page<UserDto> filteredUsers = userService.filterUsers(username, role, pageable)
                .map(userMapper::toDto);

        return ResponseEntity.ok(filteredUsers);
    }

}
