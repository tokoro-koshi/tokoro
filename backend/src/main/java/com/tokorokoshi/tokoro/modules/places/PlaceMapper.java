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
    @Mapping(target = "location.coordinate", expression = "java(new org.springframework.data.mongodb.core.geo.GeoJsonPoint(locationDto.coordinate().longitude(), locationDto.coordinate().latitude()))")
    Place toPlaceSchema(CreateUpdatePlaceDto placeDto);

    @Mapping(target = "location.coordinate.latitude", source = "location.coordinate.y")
    @Mapping(target = "location.coordinate.longitude", source = "location.coordinate.x")
    PlaceDto toPlaceDto(Place place);

    List<PlaceDto> toPlaceDto(List<Place> places);
}
