package com.tokorokoshi.tokoro.modules.user;

import com.tokorokoshi.tokoro.database.User;
import com.tokorokoshi.tokoro.modules.user.dto.CreateUpdateUserDto;
import com.tokorokoshi.tokoro.modules.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final MongoTemplate mongoTemplate;
    private final UserMapper userMapper;

    @Autowired
    public UserService(
            MongoTemplate mongoTemplate,
            UserMapper userMapper
    ) {
        this.userMapper = userMapper;
        this.mongoTemplate = mongoTemplate;
    }

    // CRUD operations

    public UserDto saveUser(CreateUpdateUserDto user) {
        User userSchema = mongoTemplate.save(
                this.userMapper.toUserSchema(user)
        );
        return this.userMapper.toUserDto(userSchema);
    }

    public UserDto getUserById(String id) {
        return this.userMapper.toUserDto(mongoTemplate.findById(id, User.class));
    }

    public List<UserDto> getAllUsers() {
        return this.userMapper.toUserDto(mongoTemplate.findAll(User.class));
    }

    public UserDto updateUser(String id, CreateUpdateUserDto user) {
        User userSchema = this.userMapper.toUserSchema(user);
        userSchema.setId(id);
        return this.userMapper.toUserDto(mongoTemplate.save(userSchema));
    }

    public void deleteUser(String id) {
        mongoTemplate.remove(userMapper.toUserSchema(getUserById(id)));
    }
}
