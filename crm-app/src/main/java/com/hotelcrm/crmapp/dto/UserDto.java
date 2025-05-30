package com.hotelcrm.crmapp.dto;

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
    private String hotel;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
