package com.tokorokoshi.tokoro.modules.places.dto;

import com.tokorokoshi.tokoro.modules.tags.dto.TagsDto;

public record PlaceDto(
    String id,
    String name,
    String description,
    LocationDto location,
    String categoryId,
    TagsDto tags,
    double rating
) {
}
