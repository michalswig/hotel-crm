package com.hotelcrm.crmapp.dto.login;

import com.hotelcrm.crmapp.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private UserDto user;
}
