package com.hotelcrm.crmapp.mapper;

import com.hotelcrm.crmapp.dto.user.UserDto;
import com.hotelcrm.crmapp.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        if (user == null) return null;

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .hotel(user.getHotel() != null ? user.getHotel().getName() : null)
                .role(user.getRole() != null ? user.getRole().getName().name() : null)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
