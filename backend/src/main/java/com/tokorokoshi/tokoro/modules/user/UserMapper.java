package com.tokorokoshi.tokoro.modules.user;

import com.tokorokoshi.tokoro.database.User;
import com.tokorokoshi.tokoro.modules.user.dto.CreateUpdateUserDto;
import com.tokorokoshi.tokoro.modules.user.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUserSchema(CreateUpdateUserDto userCreateDto);

    @Mapping(target = "userPreferencesDto", source = "preferences")
    @Mapping(target = "userFavoritesDto", source = "favorites")
    UserDto toUserDto(User userSchema);

    @Mapping(target = "userPreferencesDto", source = "preferences")
    @Mapping(target = "userFavoritesDto", source = "favorites")
    List<UserDto> toUserDto(List<User> user);
}
