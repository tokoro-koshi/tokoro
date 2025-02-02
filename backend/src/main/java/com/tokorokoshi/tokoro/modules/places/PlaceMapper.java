package com.tokorokoshi.tokoro.modules.places;

import com.tokorokoshi.tokoro.database.Place;
import com.tokorokoshi.tokoro.modules.places.dto.CreateUpdatePlaceDto;
import com.tokorokoshi.tokoro.modules.places.dto.PlaceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlaceMapper {
    @Mapping(target = "pictures", expression = "java(new java.util.ArrayList<>())")
    Place toPlaceSchema(CreateUpdatePlaceDto placeDto);

    PlaceDto toPlaceDto(Place place);

    List<PlaceDto> toPlaceDto(List<Place> places);

    Place toPlaceSchema(PlaceDto placeDto);
}
