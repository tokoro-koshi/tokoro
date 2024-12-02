package com.tokorokoshi.tokoro.modules.userRating;


import com.tokorokoshi.tokoro.database.PromptHistory;
import com.tokorokoshi.tokoro.database.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRatingService {
    MongoTemplate mongoTemplate;

    @Autowired
    public UserRatingService(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    //Crud

    public void saveUserRating(UserRating userRating){
        mongoTemplate.save(userRating);
    }

    public UserRating findUserRatingById(String id){
        return mongoTemplate.findById(id, UserRating.class);
    }

    public List<UserRating> findAllUserRatings(){
        return mongoTemplate.findAll(UserRating.class);
    }

    public void updateUserRating(String id, UserRating userRating){
        userRating.setId(id);
        mongoTemplate.save(userRating);
    }

    public void deleteUserRating(String id){
        mongoTemplate.remove(findUserRatingById(id));
    }
}
