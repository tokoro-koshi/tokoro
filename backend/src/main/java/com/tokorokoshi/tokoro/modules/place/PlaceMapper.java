package com.tokorokoshi.tokoro.modules.place;

import com.tokorokoshi.tokoro.database.Place;
import com.tokorokoshi.tokoro.modules.blog.dto.CreateUpdateBlogDto;
import com.tokorokoshi.tokoro.modules.place.dto.CreateUpdatePlaceDto;
import com.tokorokoshi.tokoro.modules.place.dto.PlaceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlaceMapper {

    @Mapping(target = "location" , source = "locationDto")
    @Mapping(target = "hashTags" , source = "hashTagsDto")
    Place toPlaceSchema(CreateUpdatePlaceDto placeDto);

    @Mapping(target = "locationDto" , source = "location")
    @Mapping(target = "hashTagsDto" , source = "hashTags")
    PlaceDto toPlaceDto(Place place);

    @Mapping(target = "location" , source = "locationDto")
    @Mapping(target = "hashTags" , source = "hashTagsDto")
    List<PlaceDto> toPlaceDto(List<Place> places);

    @Mapping(target = "location" , source = "locationDto")
    @Mapping(target = "hashTags" , source = "hashTagsDto")
    Place toPlaceSchema(PlaceDto placeDto);
}
