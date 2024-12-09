package com.tokorokoshi.tokoro.modules.places;

import com.tokorokoshi.tokoro.database.Place;
import com.tokorokoshi.tokoro.modules.places.dto.CreateUpdatePlaceDto;
import com.tokorokoshi.tokoro.modules.places.dto.PlaceDto;
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
