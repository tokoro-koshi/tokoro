package com.tokorokoshi.tokoro.modules.users;

import com.auth0.json.mgmt.users.User;
import com.tokorokoshi.tokoro.modules.users.dto.UserDto;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT
)
public interface UserMapper {
    UserDto toDto(User user);
}
