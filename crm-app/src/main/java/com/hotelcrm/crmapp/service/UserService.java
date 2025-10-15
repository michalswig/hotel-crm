package com.hotelcrm.crmapp.service;

import com.hotelcrm.crmapp.dto.user.request.UpdateUserRequest;
import com.hotelcrm.crmapp.dto.user.request.UserRequest;
import com.hotelcrm.crmapp.entity.User;

import java.util.List;

public interface UserService {
    User getById(Long id);
    List<User> getAll();
    User create(UserRequest user);
    User update(Long id, UpdateUserRequest user);
    void delete(Long id);
}
