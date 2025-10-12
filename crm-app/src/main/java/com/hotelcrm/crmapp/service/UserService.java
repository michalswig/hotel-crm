package com.hotelcrm.crmapp.service;

import com.hotelcrm.crmapp.dto.user.request.UserRequest;
import com.hotelcrm.crmapp.entity.User;

public interface UserService {
    User getById(Long id);
    User create(UserRequest user);
}
