package com.tokorokoshi.tokoro.modules.userRatings;

import com.tokorokoshi.tokoro.database.UserRating;
import com.tokorokoshi.tokoro.modules.userRatings.dto.CreateUpdateUserRatingDto;
import com.tokorokoshi.tokoro.modules.userRatings.dto.UserRatingDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserRatingMapper {

    UserRating toUserRatingSchema(CreateUpdateUserRatingDto userRatingDto);

    UserRatingDto toUserRatingDto(UserRating userRatingSchema);

    List<UserRatingDto> toUserRatingDto(List<UserRating> userRating);

    UserRating toUserRatingSchema(UserRatingDto userRatingDto);
}
