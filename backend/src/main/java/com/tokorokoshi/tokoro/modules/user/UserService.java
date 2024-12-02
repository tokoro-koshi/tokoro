package com.tokorokoshi.tokoro.modules.user;

import com.tokorokoshi.tokoro.database.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // CRUD operations

    public void saveUser(User user) {
        mongoTemplate.save(user);
    }

    public void updateUser(String id, User user) {
        user.setId(id);
        mongoTemplate.save(user);
    }

    public User getUserById(String id) {
        return mongoTemplate.findById(id, User.class);
    }

    public List<User> getAllUsers() {
        return mongoTemplate.findAll(User.class);
    }

    public void deleteUser(String id) {
        mongoTemplate.remove(getUserById(id));
    }
}
