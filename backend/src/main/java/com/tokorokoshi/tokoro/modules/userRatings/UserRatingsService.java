package com.tokorokoshi.tokoro.modules.userRatings;

import com.tokorokoshi.tokoro.database.UserRating;
import com.tokorokoshi.tokoro.modules.userRatings.dto.CreateUpdateUserRatingDto;
import com.tokorokoshi.tokoro.modules.userRatings.dto.UserRatingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for user ratings.
 */
@Service
public class UserRatingsService {
    private final MongoTemplate mongoTemplate;
    private final UserRatingMapper userRatingMapper;

    /**
     * Constructor for the user ratings service.
     * @param mongoTemplate the mongo template
     * @param userRatingMapper the user rating mapper
     */
    @Autowired
    public UserRatingsService(
            MongoTemplate mongoTemplate,
            UserRatingMapper userRatingMapper
    ) {
        this.mongoTemplate = mongoTemplate;
        this.userRatingMapper = userRatingMapper;
    }

    /**
     * Saves a user rating.
     * @param userRating the user rating to save
     * @return the saved user rating
     */
    public UserRatingDto saveUserRating(CreateUpdateUserRatingDto userRating) {
        return userRatingMapper.toUserRatingDto(
                mongoTemplate.save(
                        userRatingMapper.toUserRatingSchema(userRating)
                )
        );
    }

    /**
     * Finds a user rating by its id.
     * @param id the id of the user rating to find
     * @return the user rating
     */
    public UserRatingDto findUserRatingById(String id) {
        return userRatingMapper.toUserRatingDto(
                mongoTemplate.findById(id, UserRating.class)
        );
    }

    /**
     * Finds all user ratings.
     * @param pageable the pageable object
     * @return a page of user ratings
     */
    public Page<UserRatingDto> findAllUserRatings(Pageable pageable) {
        Query query = new Query().with(pageable);
        List<UserRating> userRatings = mongoTemplate.find(query, UserRating.class);
        long total = mongoTemplate.count(query, UserRating.class);

        List<UserRatingDto> dtos = userRatingMapper.toUserRatingDto(userRatings);
        return new PageImpl<>(dtos, pageable, total);
    }

    /**
     * Updates a user rating by its id.
     * @param id the id of the user rating to update
     * @param userRating the updated user rating
     * @return the updated user rating
     */
    public UserRatingDto updateUserRating(
            String id,
            CreateUpdateUserRatingDto userRating
    ) {
        if (findUserRatingById(id) == null) {
            return null;
        }
        UserRating schema = userRatingMapper.toUserRatingSchema(userRating);
        return userRatingMapper.toUserRatingDto(
                mongoTemplate.save(schema.withId(id))
        );

    }

    /**
     * Deletes a user rating by its id.
     * @param id the id of the user rating to delete
     */
    public void deleteUserRating(String id) {
        mongoTemplate.remove(
                userRatingMapper.toUserRatingSchema(findUserRatingById(id))
        );
    }
}
