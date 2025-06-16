package com.hotelcrm.crmapp.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginToken {
    private String accessToken;
    private String refreshToken;
}
