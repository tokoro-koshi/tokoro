package com.tokorokoshi.tokoro.modules.places;

import com.tokorokoshi.tokoro.database.HashTag;
import com.tokorokoshi.tokoro.database.Place;
import com.tokorokoshi.tokoro.modules.places.dto.CreateUpdatePlaceDto;
import com.tokorokoshi.tokoro.modules.places.dto.PlaceDto;
import com.tokorokoshi.tokoro.modules.tags.dto.TagDto;
import com.tokorokoshi.tokoro.modules.tags.dto.TagsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public interface PlaceMapper {
    @Mapping(target = "pictures", expression = "java(new java.util.ArrayList<>())")
    Place toPlaceSchema(CreateUpdatePlaceDto placeDto);

    PlaceDto toPlaceDto(Place place);

    List<PlaceDto> toPlaceDto(List<Place> places);

    default TagsDto map(List<HashTag> tags) {
        return new TagsDto(
            tags.stream()
                .map(tag -> new TagDto(tag.lang(), tag.name()))
                .toArray(TagDto[]::new)
        );
    }

    default List<HashTag> map(TagsDto tagsDto) {
        return Stream.of(tagsDto.tags())
                     .map(tagDto ->
                              new HashTag(tagDto.lang(), tagDto.name())
                     ).toList();
    }
}
