package com.hotelcrm.crmapp.util;

import com.hotelcrm.crmapp.entity.Hotel;
import com.hotelcrm.crmapp.entity.Role;
import com.hotelcrm.crmapp.entity.User;
import com.hotelcrm.crmapp.enums.RoleType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class UserDataGenerator {

    public static Hotel hotel1() {
        return Hotel.builder().id(1L).name("Hotel One").build();
    }

    public static Hotel hotel2() {
        return Hotel.builder().id(2L).name("Hotel Two").build();
    }

    public static Role adminRole() {
        return Role.builder().id(1L).name(RoleType.ADMINISTRATOR).build();
    }

    public static Role managerRole() {
        return Role.builder().id(2L).name(RoleType.MANAGER).build();
    }

    public static Role specialistRole() {
        return Role.builder().id(3L).name(RoleType.SPECIALIST).build();
    }

    public static User admin1() {
        return User.builder()
                .id(1L)
                .username("admin1")
                .password("password123")
                .hotel(hotel1())
                .role(adminRole())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static User manager1() {
        return User.builder()
                .id(2L)
                .username("manager1")
                .password("password123")
                .hotel(hotel2())
                .role(managerRole())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static User specialist1() {
        return User.builder()
                .id(3L)
                .username("specialist1")
                .password("password123")
                .hotel(hotel1())
                .role(specialistRole())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static User manager2() {
        return User.builder()
                .id(4L)
                .username("manager2")
                .password("password123")
                .hotel(hotel2())
                .role(managerRole())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static User specialist2() {
        return User.builder()
                .id(5L)
                .username("specialist2")
                .password("password123")
                .hotel(hotel2())
                .role(specialistRole())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static List<User> allUsers() {
        return Arrays.asList(admin1(), manager1(), specialist1(), manager2(), specialist2());
    }

}
