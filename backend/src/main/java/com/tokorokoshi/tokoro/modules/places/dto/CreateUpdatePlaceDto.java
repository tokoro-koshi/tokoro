package com.tokorokoshi.tokoro.modules.places.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

/**
 * A DTO for creating or updating a place
 */
@Schema(
        name = "CreateUpdatePlaceDto",
        description = "A DTO for creating or updating a place"
)
public record CreateUpdatePlaceDto(
        @Schema(
                name = "name",
                description = "The name of the place"
        )
        String name,
        @Schema(
                name = "description",
                description = "The description of the place"
        )
        String description,
        @Schema(
                name = "location",
                description = "The location of the place"
        )
        LocationDto location,
        @Schema(
                name = "categoryId",
                description = "The ID of the category of the place"
        )
        String categoryId,
        @Schema(
                name = "pictures",
                description = "The pictures of the place"
        )
        MultipartFile[] pictures,
        @Schema(
                name = "rating",
                description = "The rating of the place"
        )
        double rating
) {
}
