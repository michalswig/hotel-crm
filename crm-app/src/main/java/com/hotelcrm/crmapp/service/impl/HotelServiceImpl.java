package com.hotelcrm.crmapp.service.impl;

import com.hotelcrm.crmapp.entity.Hotel;
import com.hotelcrm.crmapp.repository.HotelRepository;
import com.hotelcrm.crmapp.service.HotelService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;

    @Override
    public Hotel getById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found: " + id));
    }

    @Override
    public Page<Hotel> getHotels(Pageable pageable) {
        return hotelRepository.findAll(pageable);
    }
}
