package com.tokorokoshi.tokoro.modules.reviews;

import com.tokorokoshi.tokoro.dto.PaginationDto;
import com.tokorokoshi.tokoro.modules.error.NotFoundException;
import com.tokorokoshi.tokoro.modules.reviews.dto.CreateUpdateReviewDto;
import com.tokorokoshi.tokoro.modules.reviews.dto.ReviewDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Reviews", description = "API for managing reviews")
@RestController
@RequestMapping("/reviews")
public class ReviewsController {

    private final ReviewsService reviewsService;
    private final PagedResourcesAssembler<ReviewDto> pagedResourcesAssembler;
    private final Logger logger;

    public ReviewsController(
            ReviewsService reviewsService,
            PagedResourcesAssembler<ReviewDto> pagedResourcesAssembler
    ) {
        this.reviewsService = reviewsService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.logger = Logger.getLogger(ReviewsController.class.getName());
    }

    @Operation(
            summary = "Save a new review",
            description = "Accepts a request with JSON data to save a new review, and returns the saved review"
    )
    @PostMapping(
            value = {"", "/"},
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ReviewDto> saveReview(
            @Parameter(description = "The review to save", required = true)
            @RequestBody
            CreateUpdateReviewDto reviewDto
    ) {
        return ResponseEntity.ok(this.reviewsService.saveReview(reviewDto));
    }

    @Operation(
            summary = "Get a review by ID",
            description = "Returns the review with the given ID"
    )
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewDto> getReviewById(
            @Parameter(
                    description = "The ID of the review to get",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String id
    ) {
        var review = this.reviewsService.getReviewById(id);
        if (review == null) {
            throw new NotFoundException("Review not found");
        }
        return ResponseEntity.ok(review);
    }

    @Operation(
            summary = "Get all reviews",
            description = "Returns a paginated list of all reviews"
    )
    @GetMapping(value = {"", "/"}, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PaginationDto<ReviewDto>> getAllReviews(
            @Parameter(description = "The page number to get", example = "0")
            @RequestParam(defaultValue = "0")
            int page,
            @Parameter(
                    description = "The number of items per page",
                    example = "20"
            )
            @RequestParam(defaultValue = "20")
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        var reviews = this.reviewsService.getAllReviews(pageable);
        var pagination = PaginationDto.fromEntityModel(
                this.pagedResourcesAssembler.toModel(reviews)
        );
        return ResponseEntity.ok(pagination);
    }

    @Operation(
            summary = "Get reviews for a specific user",
            description = "Returns a paginated list of reviews for a specific user"
    )
    @GetMapping(value = "/user/{userId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PaginationDto<ReviewDto>> getUserReviews(
            @Parameter(
                    description = "The ID of the user",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String userId,
            @Parameter(description = "The page number to get", example = "0")
            @RequestParam(defaultValue = "0")
            int page,
            @Parameter(
                    description = "The number of items per page",
                    example = "20"
            )
            @RequestParam(defaultValue = "20")
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        var reviews = this.reviewsService.getUserReviews(userId, pageable);
        var pagination = PaginationDto.fromEntityModel(
                this.pagedResourcesAssembler.toModel(reviews)
        );
        return ResponseEntity.ok(pagination);
    }

    @Operation(
            summary = "Get reviews for a specific place",
            description = "Returns a paginated list of reviews for a specific place"
    )
    @GetMapping(value = "/place/{placeId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PaginationDto<ReviewDto>> getPlaceReviews(
            @Parameter(
                    description = "The ID of the place",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String placeId,

            @Parameter(
                    description = "Whether to retrieve recommended reviews (true/false)",
                    example = "true"
            )
            @RequestParam(required = false)
            Optional<Boolean> recommended,

            @Parameter(description = "The page number to get", example = "0")
            @RequestParam(defaultValue = "0")
            int page,

            @Parameter(
                    description = "The number of items per page",
                    example = "20"
            )
            @RequestParam(defaultValue = "20")
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewDto> reviews;

        if (recommended.isPresent()) {
            reviews = this.reviewsService.getPlaceReviewsByRecommendation(placeId, recommended.get(), pageable);
        } else {
            reviews = this.reviewsService.getPlaceReviews(placeId, pageable);
        }

        var pagination = PaginationDto.fromEntityModel(
                this.pagedResourcesAssembler.toModel(reviews)
        );
        return ResponseEntity.ok(pagination);
    }

    @Operation(
            summary = "Update a review",
            description = "Accepts a request with JSON data to update a review, and returns the updated review"
    )
    @PutMapping(
            value = "/{id}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ReviewDto> updateReview(
            @Parameter(
                    description = "The ID of the review to update",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String id,
            @Parameter(description = "The review to update", required = true)
            @RequestBody
            CreateUpdateReviewDto reviewDto
    ) {
        ReviewDto updatedReview = this.reviewsService.updateReview(id, reviewDto);
        if (updatedReview == null) {
            throw new NotFoundException("Review not found");
        }
        return ResponseEntity.ok(updatedReview);
    }

    @Operation(
            summary = "Delete a review",
            description = "Deletes the review with the given ID"
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteReview(
            @Parameter(
                    description = "The ID of the review to delete",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String id
    ) {
        try {
            this.reviewsService.deleteReview(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Review not found");
        } catch (Exception e) {
            this.logger.severe(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}