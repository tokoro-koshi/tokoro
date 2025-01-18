package com.tokorokoshi.tokoro.modules.places.dto;

import java.util.List;

public record PlaceDto(
    String id,
    String name,
    String description,
    LocationDto locationDto,
    String categoryId,
    List<HashTagDto> hashTagsDto,
    double rating
) {
}
