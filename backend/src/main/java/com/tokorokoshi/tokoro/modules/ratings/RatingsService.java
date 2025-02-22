package com.tokorokoshi.tokoro.modules.ratings;

import com.tokorokoshi.tokoro.database.Rating;
import com.tokorokoshi.tokoro.modules.ratings.dto.CreateUpdateRatingDto;
import com.tokorokoshi.tokoro.modules.ratings.dto.RatingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for ratings.
 */
@Service
public class RatingsService {
    private final MongoTemplate repository;
    private final RatingMapper ratingMapper;

    /**
     * Constructor for the ratings service.
     *
     * @param repository the mongo template
     * @param ratingMapper  the rating mapper
     */
    @Autowired public RatingsService(
            MongoTemplate repository,
            RatingMapper ratingMapper
    ) {
        this.repository = repository;
        this.ratingMapper = ratingMapper;
    }

    /**
     * Saves a rating.
     *
     * @param rating the rating to save
     * @return the saved rating
     */
    public RatingDto saveRating(CreateUpdateRatingDto rating) {
        return ratingMapper.toRatingDto(repository.save(ratingMapper.toRatingSchema(
                rating)));
    }

    /**
     * Finds a rating by its id.
     *
     * @param id the id of the rating to find
     * @return the rating
     */
    public RatingDto findRatingById(String id) {
        Rating rating = repository.findById(id, Rating.class);
        if (rating == null) {
            return null;
        }
        return ratingMapper.toRatingDto(rating);
    }

    /**
     * Finds all ratings.
     *
     * @param pageable the pageable object  (required)
     * @param userId   the user id          (optional)
     * @param placeId  the place id         (optional)
     * @return a page of ratings
     */
    public Page<RatingDto> findAllRatings(
            Pageable pageable,
            String userId,
            String placeId
    ) {
        Query query = new Query().with(pageable);
        if (userId != null) {
            query.addCriteria(Criteria.where("userId").is(userId));
        }
        if (placeId != null) {
            query.addCriteria(Criteria.where("placeId").is(placeId));
        }
        List<Rating> ratings = repository.find(query, Rating.class);
        long total = repository.count(query, Rating.class);

        List<RatingDto> dtos = ratingMapper.toRatingDto(ratings);
        return new PageImpl<>(dtos, pageable, total);
    }

    /**
     * Updates a rating by its id.
     *
     * @param id     the id of the rating to update
     * @param rating the updated rating
     * @return the updated rating
     */
    public RatingDto updateRating(String id, CreateUpdateRatingDto rating) {
        if (findRatingById(id) == null) {
            return null;
        }
        Rating schema = ratingMapper.toRatingSchema(rating);
        return ratingMapper.toRatingDto(repository.save(schema.withId(id)));

    }

    /**
     * Deletes a rating by its id.
     *
     * @param id the id of the rating to delete
     */
    public void deleteRating(String id) {
        repository.remove(ratingMapper.toRatingSchema(findRatingById(id)));
    }
}
