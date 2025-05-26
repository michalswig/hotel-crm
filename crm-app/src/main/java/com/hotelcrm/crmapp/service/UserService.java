package com.hotelcrm.crmapp.service;

import com.hotelcrm.crmapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<User> getUsers(Pageable pageable);
    public User getById(Long id);
    public Page<User> filterUsers(String username, String role, Pageable pageable);
}
