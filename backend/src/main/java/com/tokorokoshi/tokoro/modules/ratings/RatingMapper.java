package com.tokorokoshi.tokoro.modules.ratings;

import com.tokorokoshi.tokoro.database.Rating;
import com.tokorokoshi.tokoro.modules.ratings.dto.CreateUpdateRatingDto;
import com.tokorokoshi.tokoro.modules.ratings.dto.RatingDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    Rating toRatingSchema(CreateUpdateRatingDto ratingDto);

    RatingDto toRatingDto(Rating ratingSchema);

    List<RatingDto> toRatingDto(List<Rating> rating);

    Rating toRatingSchema(RatingDto ratingDto);
}
