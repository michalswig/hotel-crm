package com.hotelcrm.crmapp.dto.user;

import com.hotelcrm.crmapp.entity.Hotel;
import com.hotelcrm.crmapp.entity.Role;
import com.hotelcrm.crmapp.entity.User;

import java.util.List;

public class UserMapper {
    public static UserResponse toResponse(User user) {
        if (user == null) return null;

        Hotel hotel = user.getHotel();
        Role role = user.getRole();

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .hotelId(hotel != null ? hotel.getId() : null)
                .hotelName(hotel != null ? hotel.getName() : null)
                .roleId(role != null ? role.getId() : null)
                .role(role != null && role.getName() != null ? role.getName().name() : null)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static List<UserResponse> toResponseList(List<User> users) {
        return users == null ? List.of() : users.stream()
                .map(UserMapper::toResponse)
                .toList();
    }
}
