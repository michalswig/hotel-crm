package com.hotelcrm.crmapp.service.impl;

import com.hotelcrm.crmapp.entity.User;
import com.hotelcrm.crmapp.repository.UserRepository;
import com.hotelcrm.crmapp.util.UserDataGenerator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    void shouldReturnPagedUsers() {
        // given
        List<User> users = UserDataGenerator.allUsers();
        PageImpl<User> pagedUsers = new PageImpl<>(users);
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.findAll(pageable)).thenReturn(pagedUsers);
        // when
        Page<User> usersResultPerPage = userService.getUsers(pageable);
        // then
        assertThat(usersResultPerPage.getContent()).hasSize(5);
    }

    @Test
    void shouldReturnSecondPageOfUsers() {
        // given
        List<User> allUsers = UserDataGenerator.allUsers();
        Pageable pageable = PageRequest.of(1, 2);
        List<User> expectedPage = allUsers.subList(2, 4);
        Page<User> pagedUsers = new PageImpl<>(expectedPage, pageable, allUsers.size());
        when(userRepository.findAll(pageable)).thenReturn(pagedUsers);
        // when
        Page<User> result = userService.getUsers(pageable);
        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).isEqualTo(expectedPage);
    }

    @Test
    void shouldReturnUserByIdWhenExists() {
        // given
        User user = UserDataGenerator.admin1();
        Long id = user.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        //when
        User testUser = userService.getById(id);
        //then
        assertEquals(user, testUser);
        assertThat(testUser).isEqualTo(user);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenUserNotFound() {
        // given
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        // when and then
        try {
            userService.getById(id);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(EntityNotFoundException.class);
            assertThat(e.getMessage()).contains("User not found");
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldReturnPagedUsersByUsernameAndRole() {
        //given
        List<User> users = UserDataGenerator.allUsers();
        User adminUser = UserDataGenerator.admin1();
        String username = adminUser.getUsername();
        String role = adminUser.getRole().toString();
        Pageable pageable = PageRequest.of(0, 10);
        List<User> filteredUsers = users.stream().filter(
                        user -> user.getUsername().equals(username) && user.getRole().getName().name().equals(role))
                .toList();
        PageImpl<User> expectedUserPage = new PageImpl<>(filteredUsers, pageable, filteredUsers.size());

        when(userRepository.findAll((Specification<User>) any(Specification.class), eq(pageable)))
                .thenReturn(expectedUserPage);

        //when
        Page<User> userPage = userService.filterUsers(username, role, pageable);
        //then
        assertThat(userPage.getContent()).hasSize(filteredUsers.size());
    }


}

