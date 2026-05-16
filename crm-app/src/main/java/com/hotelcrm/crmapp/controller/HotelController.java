package com.hotelcrm.crmapp.controller;

import com.hotelcrm.crmapp.dto.hotel.HotelMapper;
import com.hotelcrm.crmapp.dto.hotel.HotelResponse;
import com.hotelcrm.crmapp.entity.Hotel;
import com.hotelcrm.crmapp.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @Operation(summary = "Get hotel by ID", description = "Returns details of a specific hotel by ID")
    @ApiResponse(responseCode = "200", description = "Hotel successfully retrieved")
    @ApiResponse(responseCode = "404", description = "Hotel not found")
    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> getHotelById(
            @Parameter(description = "ID of the hotel to retrieve") @PathVariable Long id) {

        Hotel hotel = hotelService.getById(id);
        return ResponseEntity.ok(HotelMapper.toResponse(hotel));
    }

    @Operation(summary = "Get all hotels", description = "Returns all hotels")
    @ApiResponse(responseCode = "200", description = "Hotels successfully retrieved")
    @ApiResponse(responseCode = "404", description = "Hotels not found")
    @GetMapping("/all")
    public java.util.List<HotelResponse> getAll() {
        return hotelService.getHotels(Pageable.unpaged())
                .map(HotelMapper::toResponse)
                .toList();
    }

}
