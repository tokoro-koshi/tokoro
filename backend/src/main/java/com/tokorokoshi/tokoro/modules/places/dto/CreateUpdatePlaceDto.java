package com.tokorokoshi.tokoro.modules.places.dto;

import java.util.List;

public record CreateUpdatePlaceDto(
    String name,
    String description,
    LocationDto location,
    String categoryId,
    List<String> pictures,
    double rating
) {
}
