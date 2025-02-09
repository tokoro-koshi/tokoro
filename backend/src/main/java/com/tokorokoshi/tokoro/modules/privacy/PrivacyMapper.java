package com.tokorokoshi.tokoro.modules.privacy;

import com.tokorokoshi.tokoro.database.Privacy;
import com.tokorokoshi.tokoro.modules.privacy.dto.CreateUpdatePrivacyDto;
import com.tokorokoshi.tokoro.modules.privacy.dto.PrivacyDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrivacyMapper {
    @Mapping(target = "lastUpdated", ignore = true)
    Privacy toPrivacy(CreateUpdatePrivacyDto createUpdatePrivacyDto);

    PrivacyDto toPrivacyDto(Privacy privacy);
}