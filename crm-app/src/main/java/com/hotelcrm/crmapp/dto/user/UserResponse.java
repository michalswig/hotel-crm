package com.hotelcrm.crmapp.dto.user;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;

    private Long hotelId;
    private String hotelName;

    private Long roleId;
    private String role;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}