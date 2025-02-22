package com.tokorokoshi.tokoro.modules.reviews;

import com.tokorokoshi.tokoro.database.Review;
import com.tokorokoshi.tokoro.modules.reviews.dto.CreateUpdateReviewDto;
import com.tokorokoshi.tokoro.modules.reviews.dto.ReviewDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Review toReviewSchema(CreateUpdateReviewDto reviewDto);

    ReviewDto toReviewDto(Review review);

    List<ReviewDto> toReviewDto(List<Review> reviews);
}

