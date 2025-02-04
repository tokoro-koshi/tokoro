package com.tokorokoshi.tokoro.modules.places.dto;

import org.springframework.web.multipart.MultipartFile;

public record CreateUpdatePlaceDto(
    String name,
    String description,
    LocationDto location,
    String categoryId,
    MultipartFile[] pictures,
    double rating
) {
}
