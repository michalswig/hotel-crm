package com.hotelcrm.crmapp.service;

import com.hotelcrm.crmapp.config.CustomUserDetails;
import com.hotelcrm.crmapp.dto.LoginRequest;
import com.hotelcrm.crmapp.dto.LoginResponse;
import com.hotelcrm.crmapp.dto.UserDto;
import com.hotelcrm.crmapp.exception.InvalidCredentialsException;
import com.hotelcrm.crmapp.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public LoginResponse authenticate(LoginRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException ex) {
            log.warn("Authentication failed for username: {}", request.getUsername());
            throw new InvalidCredentialsException("Invalid username or password");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwtToken = jwtService.generateToken(userDetails.getUsername());
        UserDto userDto = userMapper.toDto(userDetails.getUser());

        return new LoginResponse(jwtToken, userDto);
    }
}