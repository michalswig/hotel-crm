package com.hotelcrm.crmapp.service.impl;

import com.hotelcrm.crmapp.entity.User;
import com.hotelcrm.crmapp.enums.RoleType;
import com.hotelcrm.crmapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class UserServiceImplIntegrationTest {
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldReturnAllUsersPaged() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        //when
        Page<User> users = userServiceImpl.getUsers(pageable);
        //then
        assertEquals(5, users.getContent().size());
    }
    @Test
    void shouldFilterUsersByUsername() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        //when
        Page<User> usersPage = userServiceImpl.filterUsers("admin1", null, pageable);
        //then
        assertThat(usersPage.getContent().getFirst().getUsername()).isEqualTo("admin1");
    }

    @Test
    void shouldFilterUsersByRole() {
        //given
        String role = "ADMINISTRATOR";
        Pageable pageable = PageRequest.of(0, 10);
        //when
        Page<User> usersPage = userServiceImpl.filterUsers(null, role, pageable);
        //then
        assertThat(usersPage.getContent().getFirst().getRole().getName())
                .isEqualTo(RoleType.ADMINISTRATOR);
    }

}
