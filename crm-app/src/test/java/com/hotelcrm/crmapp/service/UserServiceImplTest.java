package com.hotelcrm.crmapp.service;

import com.hotelcrm.crmapp.dto.user.request.UserRequest;
import com.hotelcrm.crmapp.entity.Hotel;
import com.hotelcrm.crmapp.entity.Role;
import com.hotelcrm.crmapp.entity.User;
import com.hotelcrm.crmapp.enums.RoleType;
import com.hotelcrm.crmapp.repository.HotelRepository;
import com.hotelcrm.crmapp.repository.RoleRepository;
import com.hotelcrm.crmapp.repository.UserRepository;
import com.hotelcrm.crmapp.service.impl.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private HotelRepository hotelRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private Hotel hotel;
    private Role role;

    @BeforeEach
    void setUp() {
        hotel = Hotel.builder().id(1L).name("Hotel Test").build();
        role = Role.builder().id(2L).name(RoleType.MANAGER).build();
    }

    @Test
    void delete_whenValidId_shouldDelete() {
        User user = User.builder()
                .id(1L)
                .username("testName")
                .password("testPassword")
                .build();
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        //when
        userService.delete(1L);
        //then
        verify(userRepository).delete(user);
    }

    @Test
    void delete_shouldThrowException_whenUserNotFound() {
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        //when then
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> userService.delete(1L));
        assertEquals("User not found: 1", entityNotFoundException.getMessage());
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void create_whenValidRequest_shouldReturnCreatedUser() {
        // Arrange
        UserRequest req = UserRequest.builder()
                .userName("John.Doe")
                .password("Password1")
                .hotelId(1L)
                .roleId(2L)
                .build();

        when(userRepository.existsByUsernameIgnoreCase("john.doe")).thenReturn(false);
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(roleRepository.findById(2L)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("Password1")).thenReturn("ENCODED");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User user = inv.getArgument(0, User.class);
            user.setId(10L);
            // simulate JPA setting timestamps if needed
            if (user.getCreatedAt() == null) {
                user.setCreatedAt(LocalDateTime.now());
            }
            return user;
        });

        // Act
        User created = userService.create(req);

        // Assert
        assertThat(created.getId()).isEqualTo(10L);
        assertThat(created.getUsername()).isEqualTo("john.doe");
        assertThat(created.getPassword()).isEqualTo("ENCODED");
        assertThat(created.getHotel()).isSameAs(hotel);
        assertThat(created.getRole()).isSameAs(role);
        assertThat(created.getCreatedAt()).isNotNull();

        verify(userRepository).existsByUsernameIgnoreCase("john.doe");
        verify(passwordEncoder).encode("Password1");
        verify(userRepository).save(userCaptor.capture());
        User saved = userCaptor.getValue();
        assertThat(saved.getUsername()).isEqualTo("john.doe");
    }
}
