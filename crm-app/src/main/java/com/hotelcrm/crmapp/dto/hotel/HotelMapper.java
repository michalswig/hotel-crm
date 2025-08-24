package com.hotelcrm.crmapp.dto.hotel;

import com.hotelcrm.crmapp.entity.Hotel;

public final class HotelMapper {
    private HotelMapper() {}

    public static HotelResponse toResponse(Hotel hotel) {
        if (hotel == null) return null;
        return HotelResponse.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .build();
    }
}
