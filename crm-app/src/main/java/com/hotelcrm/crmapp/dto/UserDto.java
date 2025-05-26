package com.hotelcrm.crmapp.dto;

import com.hotelcrm.crmapp.entity.Hotel;
import com.hotelcrm.crmapp.entity.Role;
import com.hotelcrm.crmapp.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Hotel hotel;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserDto(Long id, String username, Hotel hotel, Role role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.hotel = hotel;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .hotel(user.getHotel())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

}
