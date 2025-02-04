package com.tokorokoshi.tokoro.modules.places.dto;

import com.tokorokoshi.tokoro.modules.tags.dto.TagDto;

import java.util.List;

public record PlaceDto(
    String id,
    String name,
    String description,
    LocationDto location,
    String categoryId,
    List<TagDto> tags,
    List<String> pictures,
    double rating
) {
}
