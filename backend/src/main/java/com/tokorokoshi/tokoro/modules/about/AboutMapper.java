package com.tokorokoshi.tokoro.modules.about;

import com.tokorokoshi.tokoro.database.About;
import com.tokorokoshi.tokoro.modules.about.dto.AboutDto;
import com.tokorokoshi.tokoro.modules.about.dto.CreateUpdateAboutDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AboutMapper {
    @Mapping(target = "logo", expression = "java(new String())")
    About toAbout(CreateUpdateAboutDto createUpdateAboutDto);

    AboutDto toAboutDto(About about);
}