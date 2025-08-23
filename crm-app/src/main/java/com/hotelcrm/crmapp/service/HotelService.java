package com.hotelcrm.crmapp.service;

import com.hotelcrm.crmapp.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HotelService {
    Hotel getById(Long id);
    Page<Hotel> getHotels(Pageable pageable);
}
