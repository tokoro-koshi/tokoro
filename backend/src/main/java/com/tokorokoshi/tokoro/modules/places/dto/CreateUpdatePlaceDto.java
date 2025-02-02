package com.tokorokoshi.tokoro.modules.places.dto;

public record CreateUpdatePlaceDto(
    String name,
    String description,
    LocationDto location,
    String categoryId,
    double rating
) {
}
