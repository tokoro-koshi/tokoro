package com.tokorokoshi.tokoro.modules.places.dto;

import com.tokorokoshi.tokoro.modules.tags.dto.TagDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * A DTO for a place
 */
@Schema(
        name = "PlaceDto",
        description = "A DTO for a place"
)
public record PlaceDto(
        @Schema(
                name = "id",
                description = "The ID of the place"
        )
        String id,
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
                name = "tags",
                description = "The tags of the place"
        )
        List<TagDto> tags,
        @Schema(
                name = "pictures",
                description = "The pictures of the place"
        )
        List<String> pictures,
        @Schema(
                name = "rating",
                description = "The rating of the place"
        )
        double rating
) {
}
