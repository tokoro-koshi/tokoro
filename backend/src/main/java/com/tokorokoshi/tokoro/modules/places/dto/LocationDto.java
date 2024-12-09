package com.tokorokoshi.tokoro.modules.places.dto;

public record LocationDto(
        String address,
        String city,
        String country,
        CoordinateDto coordinate
) {
}
