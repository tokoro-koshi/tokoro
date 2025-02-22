package com.tokorokoshi.tokoro.modules.reviews;

import com.tokorokoshi.tokoro.database.Review;
import com.tokorokoshi.tokoro.modules.auth0.Auth0UserDataService;
import com.tokorokoshi.tokoro.modules.reviews.dto.CreateUpdateReviewDto;
import com.tokorokoshi.tokoro.modules.reviews.dto.ReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ReviewsService {

    private final MongoTemplate mongoTemplate;
    private final ReviewMapper reviewMapper;
    private final Auth0UserDataService auth0UserDataService;

    @Autowired
    public ReviewsService(
            MongoTemplate mongoTemplate,
            ReviewMapper reviewMapper,
            Auth0UserDataService auth0UserDataService
    ) {
        this.mongoTemplate = mongoTemplate;
        this.reviewMapper = reviewMapper;
        this.auth0UserDataService = auth0UserDataService;
    }

    /**
     * Saves a new review.
     *
     * @param createUpdateReviewDto review data
     * @return the saved review
     */
    public ReviewDto saveReview(CreateUpdateReviewDto createUpdateReviewDto) {
        String userId = auth0UserDataService.getAuthenticatedUserId();

        Review review = reviewMapper.toReviewSchema(createUpdateReviewDto);
        review = review.withUserId(userId);
        Review savedReview = mongoTemplate.save(review);

        return reviewMapper.toReviewDto(savedReview);
    }

    /**
     * Retrieves a review by ID.
     *
     * @param id review ID
     * @return the review
     */
    public ReviewDto getReviewById(String id) {
        Review review = mongoTemplate.findById(id, Review.class);
        if (review == null) return null;

        return reviewMapper.toReviewDto(review);
    }

    /**
     * Retrieves paginated reviews.
     *
     * @param pageable pagination information (page, size)
     * @return paginated list of reviews
     */
    public Page<ReviewDto> getAllReviews(Pageable pageable) {
        // Create a query with pagination information
        Query query = Query.query(new Criteria()).with(pageable);
        List<Review> reviews = mongoTemplate.find(query, Review.class);

        // Count the total number of reviews
        long total = mongoTemplate.count(new Query(), Review.class);

        List<ReviewDto> content = reviews.stream()
                .map(reviewMapper::toReviewDto)
                .toList();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * Deletes a review by ID.
     *
     * @param id review ID
     */
    public void deleteReview(String id) {
        String userId = auth0UserDataService.getAuthenticatedUserId();

        Review review = mongoTemplate.findById(id, Review.class);
        if (review == null) {
            throw new IllegalArgumentException("Review not found for id: " + id);
        }

        // Check if the user is authorized to delete the review
        if (!Objects.equals(review.userId(), userId)) {
            throw new IllegalArgumentException("User is not authorized to delete this review");
        }

        mongoTemplate.remove(review);
    }

    /**
     * Retrieves reviews for a specific user.
     *
     * @param userId   user ID
     * @param pageable pagination information (page, size)
     * @return paginated list of reviews for the user
     */
    public Page<ReviewDto> getUserReviews(String userId, Pageable pageable) {
        // Create a query with pagination information and filter by user ID
        Query query = Query.query(Criteria.where("userId").is(userId)).with(pageable);

        List<Review> reviews = mongoTemplate.find(query, Review.class);

        // Count the total number of reviews for the user
        long total = mongoTemplate.count(Query.query(Criteria.where("userId").is(userId)), Review.class);

        List<ReviewDto> content = reviews.stream()
                .map(reviewMapper::toReviewDto)
                .toList();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * Retrieves reviews for a specific place.
     *
     * @param placeId  place ID
     * @param pageable pagination information (page, size)
     * @return paginated list of reviews for the place
     */
    public Page<ReviewDto> getPlaceReviews(String placeId, Pageable pageable) {
        // Create a query with pagination information and filter by place ID
        Query query = Query.query(Criteria.where("placeId").is(placeId)).with(pageable);

        List<Review> reviews = mongoTemplate.find(query, Review.class);

        // Count the total number of reviews for the place
        long total = mongoTemplate.count(Query.query(Criteria.where("placeId").is(placeId)), Review.class);

        List<ReviewDto> content = reviews.stream()
                .map(reviewMapper::toReviewDto)
                .toList();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * Retrieves reviews for a specific place based on the recommendation status.
     *
     * @param placeId     place ID
     * @param recommended whether to retrieve recommended or non-recommended reviews
     * @param pageable    pagination information (page, size)
     * @return paginated list of reviews for the place based on the recommendation status
     */
    public Page<ReviewDto> getPlaceReviewsByRecommendation(String placeId, boolean recommended, Pageable pageable) {
        // Create a query with pagination information and filter by place ID and recommended flag
        Query query = Query.query(Criteria.where("placeId").is(placeId).and("recommended").is(recommended)).with(pageable);

        List<Review> reviews = mongoTemplate.find(query, Review.class);

        // Count the total number of reviews for the place based on the recommendation status
        long total = mongoTemplate.count(Query.query(Criteria.where("placeId").is(placeId).and("recommended").is(recommended)), Review.class);

        List<ReviewDto> content = reviews.stream()
                .map(reviewMapper::toReviewDto)
                .toList();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * Updates an existing review.
     *
     * @param id                    review ID
     * @param createUpdateReviewDto review data to update
     * @return the updated review
     */
    public ReviewDto updateReview(String id, CreateUpdateReviewDto createUpdateReviewDto) {
        String userId = auth0UserDataService.getAuthenticatedUserId();

        Review existingReview = mongoTemplate.findById(id, Review.class);
        if (existingReview == null) {
            throw new IllegalArgumentException("Review not found for id: " + id);
        }

        // Check if the user is authorized to update the review
        if (!Objects.equals(existingReview.userId(), userId)) {
            throw new IllegalArgumentException("User is not authorized to update this review");
        }

        Review review = reviewMapper.toReviewSchema(createUpdateReviewDto)
                .withId(existingReview.id())
                .withUserId(userId)
                .withCreatedAt(existingReview.createdAt());
        Review savedReview = mongoTemplate.save(review);

        return reviewMapper.toReviewDto(savedReview);
    }
}