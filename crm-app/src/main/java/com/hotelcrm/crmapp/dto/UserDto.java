package com.hotelcrm.crmapp.dto;

import com.hotelcrm.crmapp.entity.Hotel;
import com.hotelcrm.crmapp.entity.Role;
import com.hotelcrm.crmapp.entity.User;
import lombok.*;

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

    public UserDto(Long id, String username, Hotel hotel, Role role) {
        this.id = id;
        this.username = username;
        this.hotel = hotel;
        this.role = role;
    }

    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .hotel(user.getHotel())
                .role(user.getRole())
                .build();
    }

}
