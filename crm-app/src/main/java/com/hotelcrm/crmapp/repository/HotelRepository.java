package com.hotelcrm.crmapp.repository;

import com.hotelcrm.crmapp.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
