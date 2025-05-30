package com.hotelcrm.crmapp.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Test {
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Plain text password entered by user (e.g. from login form)
        String rawPassword = "password123";

        // Hashed password from the database (replace with your actual hash)
        String hashedPassword = "$2a$10$cCSQo0GITmCZBA8Q72KTsem0/gECr.BXLEbVuvGSSpntiuNQKmQ8.";

        // Check if raw password matches the hashed password
        boolean matches = passwordEncoder.matches(rawPassword, hashedPassword);

        System.out.println("Password matches: " + matches);    }
}
