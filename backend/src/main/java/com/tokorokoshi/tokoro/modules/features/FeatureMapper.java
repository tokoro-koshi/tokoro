package com.tokorokoshi.tokoro.modules.features;

import com.tokorokoshi.tokoro.database.Feature;
import com.tokorokoshi.tokoro.modules.features.dto.CreateUpdateFeatureDto;
import com.tokorokoshi.tokoro.modules.features.dto.FeatureDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FeatureMapper {
    @Mapping(target = "picture", expression = "java(new String())")
    Feature toFeatureScheme(CreateUpdateFeatureDto createUpdateFeatureDto);

    FeatureDto toFeatureDto(Feature feature);

    List<FeatureDto> toFeatureDto(List<Feature> features);
}
