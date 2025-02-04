package com.tokorokoshi.tokoro.modules.ratings;

import com.tokorokoshi.tokoro.database.Rating;
import com.tokorokoshi.tokoro.modules.ratings.dto.CreateUpdateRatingDto;
import com.tokorokoshi.tokoro.modules.ratings.dto.RatingDto;
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
public class RatingsService {
    private final MongoTemplate mongoTemplate;
    private final RatingMapper ratingMapper;

    /**
     * Constructor for the user ratings service.
     *
     * @param mongoTemplate the mongo template
     * @param ratingMapper  the user rating mapper
     */
    @Autowired
    public RatingsService(
            MongoTemplate mongoTemplate,
            RatingMapper ratingMapper
    ) {
        this.mongoTemplate = mongoTemplate;
        this.ratingMapper = ratingMapper;
    }

    /**
     * Saves a user rating.
     *
     * @param rating the user rating to save
     * @return the saved user rating
     */
    public RatingDto saveRating(CreateUpdateRatingDto rating) {
        return ratingMapper.toRatingDto(
                mongoTemplate.save(
                        ratingMapper.toRatingSchema(rating)
                )
        );
    }

    /**
     * Finds a user rating by its id.
     *
     * @param id the id of the user rating to find
     * @return the user rating
     */
    public RatingDto findRatingById(String id) {
        Rating rating = mongoTemplate.findById(id, Rating.class);
        if (rating == null) {
            return null;
        }
        return ratingMapper.toRatingDto(rating);
    }

    /**
     * Finds all user ratings.
     *
     * @param pageable the pageable object
     * @return a page of user ratings
     */
    public Page<RatingDto> findAllRatings(Pageable pageable) {
        Query query = new Query().with(pageable);
        List<Rating> ratings = mongoTemplate.find(query, Rating.class);
        long total = mongoTemplate.count(query, Rating.class);

        List<RatingDto> dtos = ratingMapper.toRatingDto(ratings);
        return new PageImpl<>(dtos, pageable, total);
    }

    /**
     * Updates a user rating by its id.
     *
     * @param id     the id of the user rating to update
     * @param rating the updated user rating
     * @return the updated user rating
     */
    public RatingDto updateRating(
            String id,
            CreateUpdateRatingDto rating
    ) {
        if (findRatingById(id) == null) {
            return null;
        }
        Rating schema = ratingMapper.toRatingSchema(rating);
        return ratingMapper.toRatingDto(
                mongoTemplate.save(schema.withId(id))
        );

    }

    /**
     * Deletes a user rating by its id.
     *
     * @param id the id of the user rating to delete
     */
    public void deleteRating(String id) {
        mongoTemplate.remove(
                ratingMapper.toRatingSchema(findRatingById(id))
        );
    }
}
