package com.tokorokoshi.tokoro.modules.reviews;

import com.tokorokoshi.tokoro.database.Review;
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

@Service
public class ReviewsService {

    private final MongoTemplate repository;
    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewsService(
            MongoTemplate repository,
            ReviewMapper reviewMapper
    ) {
        this.repository = repository;
        this.reviewMapper = reviewMapper;
    }

    /**
     * Saves a new review.
     *
     * @param createUpdateReviewDto review data
     * @return the saved review
     */
    public ReviewDto saveReview(CreateUpdateReviewDto createUpdateReviewDto) {
        Review review = reviewMapper.toReviewSchema(createUpdateReviewDto);
        Review savedReview = repository.save(review);

        return reviewMapper.toReviewDto(savedReview);
    }

    /**
     * Retrieves a review by ID.
     *
     * @param id review ID
     * @return the review
     */
    public ReviewDto getReviewById(String id) {
        Review review = repository.findById(id, Review.class);
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
        List<Review> reviews = repository.find(query, Review.class);

        // Count the total number of reviews
        long total = repository.count(new Query(), Review.class);

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
        Review review = repository.findById(id, Review.class);
        if (review == null) {
            throw new IllegalArgumentException("Review not found for id: " + id);
        }

        repository.remove(review);
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

        List<Review> reviews = repository.find(query, Review.class);

        // Count the total number of reviews for the user
        long total = repository.count(Query.query(Criteria.where("userId").is(userId)), Review.class);

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

        List<Review> reviews = repository.find(query, Review.class);

        // Count the total number of reviews for the place
        long total = repository.count(Query.query(Criteria.where("placeId").is(placeId)), Review.class);

        List<ReviewDto> content = reviews.stream()
                .map(reviewMapper::toReviewDto)
                .toList();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * Retrieves reviews for a specific place based on the recommendation status.
     *
     * @param placeId       place ID
     * @param isRecommended whether to retrieve recommended or non-recommended reviews
     * @param pageable      pagination information (page, size)
     * @return paginated list of reviews for the place based on the recommendation status
     */
    public Page<ReviewDto> getPlaceReviewsByRecommendation(String placeId, boolean isRecommended, Pageable pageable) {
        // Create a query with pagination information and filter by place ID and recommended flag
        Query query = Query.query(Criteria.where("placeId").is(placeId).and("recommended").is(isRecommended)).with(pageable);

        List<Review> reviews = repository.find(query, Review.class);

        // Count the total number of reviews for the place based on the recommendation status
        long total = repository.count(Query.query(Criteria.where("placeId").is(placeId).and("recommended").is(isRecommended)), Review.class);

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
        Review existingReview = repository.findById(id, Review.class);
        if (existingReview == null) {
            throw new IllegalArgumentException("Review not found for id: " + id);
        }

        Review review = reviewMapper.toReviewSchema(createUpdateReviewDto)
                .withId(existingReview.id())
                .withCreatedAt(existingReview.createdAt());
        Review savedReview = repository.save(review);

        return reviewMapper.toReviewDto(savedReview);
    }
}
