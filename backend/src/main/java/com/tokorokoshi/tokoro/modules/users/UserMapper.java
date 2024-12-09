package com.tokorokoshi.tokoro.modules.users;

import com.tokorokoshi.tokoro.database.User;
import com.tokorokoshi.tokoro.modules.users.dto.CreateUpdateUserDto;
import com.tokorokoshi.tokoro.modules.users.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "preferences", source = "userPreferencesDto")
    @Mapping(target = "favorites", source = "userFavoritesDto")
    User toUserSchema(CreateUpdateUserDto userCreateDto);

    @Mapping(target = "userPreferencesDto", source = "preferences")
    @Mapping(target = "userFavoritesDto", source = "favorites")
    UserDto toUserDto(User userSchema);

    @Mapping(target = "userPreferencesDto", source = "preferences")
    @Mapping(target = "userFavoritesDto", source = "favorites")
    List<UserDto> toUserDto(List<User> user);

    @Mapping(target = "preferences", source = "userPreferencesDto")
    @Mapping(target = "favorites", source = "userFavoritesDto")
    User toUserSchema(UserDto userDto);
}
