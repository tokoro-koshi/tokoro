package com.tokorokoshi.tokoro.modules.userRatings;

import com.tokorokoshi.tokoro.database.UserRating;
import com.tokorokoshi.tokoro.modules.userRatings.dto.CreateUpdateUserRatingDto;
import com.tokorokoshi.tokoro.modules.userRatings.dto.UserRatingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRatingsService {
    private final MongoTemplate mongoTemplate;
    private final UserRatingMapper userRatingMapper;

    @Autowired
    public UserRatingsService(MongoTemplate mongoTemplate, UserRatingMapper userRatingMapper) {
        this.mongoTemplate = mongoTemplate;
        this.userRatingMapper = userRatingMapper;
    }


    //Crud

    public UserRatingDto saveUserRating(CreateUpdateUserRatingDto userRating) {
        return userRatingMapper.
                toUserRatingDto(mongoTemplate.
                        save(userRatingMapper.
                                toUserRatingSchema(userRating)));
    }

    public UserRatingDto findUserRatingById(String id) {
        return userRatingMapper.
                toUserRatingDto(mongoTemplate.
                        findById(id, UserRating.class));
    }

    public List<UserRatingDto> findAllUserRatings() {
        return userRatingMapper.
                toUserRatingDto(mongoTemplate.
                        findAll(UserRating.class));
    }

    public UserRatingDto updateUserRating(String id, CreateUpdateUserRatingDto userRating) {
        if (findUserRatingById(id) == null) {
            return null;
        }
        UserRating userRatingSchema = userRatingMapper.toUserRatingSchema(userRating);
        return userRatingMapper.toUserRatingDto(
                mongoTemplate.save(userRatingSchema.withId(id))
        );

    }

    public void deleteUserRating(String id) {
        mongoTemplate.remove(userRatingMapper.toUserRatingSchema(findUserRatingById(id)));
    }
}
