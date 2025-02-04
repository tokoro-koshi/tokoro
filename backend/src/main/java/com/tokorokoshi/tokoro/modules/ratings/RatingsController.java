package com.tokorokoshi.tokoro.modules.ratings;

import com.tokorokoshi.tokoro.modules.ratings.dto.CreateUpdateRatingDto;
import com.tokorokoshi.tokoro.modules.ratings.dto.RatingDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(
        name = "Places Ratings",
        description = "API for managing user ratings of places"
)
@RestController
@RequestMapping("/api/user-ratings")
public class RatingsController {
    private final RatingsService ratingsService;
    private final Logger logger;

    @Autowired
    public RatingsController(RatingsService ratingsService) {
        this.ratingsService = ratingsService;
        this.logger = Logger.getLogger(RatingsController.class.getName());
    }

    @Operation(
            summary = "Create a new user rating",
            description = "Accepts a request with a JSON body to create a new user rating, and returns the created user rating"
    )
    @PostMapping(
            value = {"", "/"},
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<RatingDto> create(
            @Parameter(
                    description = "The user rating to create",
                    required = true
            )
            @RequestBody
            CreateUpdateRatingDto rating
    ) {
        return ResponseEntity.ok(
                this.ratingsService.saveRating(rating)
        );
    }

    @Operation(
            summary = "Get a user rating by ID",
            description = "Returns the user rating with the given ID"
    )
    @GetMapping(
            value = "/{id}",
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<RatingDto> get(
            @Parameter(
                    description = "The ID of the user rating to get",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        var rating = this.ratingsService.findRatingById(id);
        if (rating == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rating);
    }

    @Operation(
            summary = "Get all user ratings",
            description = "Returns a paginated list of all user ratings"
    )
    @GetMapping(
            value = {"", "/"},
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Page<RatingDto>> getAllRatings(
            @Parameter(
                    description = "The page number to get",
                    example = "0"
            )
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
        return ResponseEntity.ok(
                this.ratingsService.findAllRatings(pageable)
        );
    }

    @Operation(
            summary = "Update a user rating",
            description = "Accepts a request with a JSON body to update a user rating, and returns the updated user rating"
    )
    @PutMapping(
            value = "/{id}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<RatingDto> update(
            @Parameter(
                    description = "The user rating to update",
                    required = true
            )
            @RequestBody
            CreateUpdateRatingDto rating,
            @Parameter(
                    description = "The ID of the user rating to update",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        RatingDto updatedRating = this.ratingsService
                .updateRating(id, rating);
        if (updatedRating == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedRating);
    }

    @Operation(
            summary = "Delete a user rating",
            description = "Deletes the user rating with the given ID"
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(
            @Parameter(
                    description = "The ID of the user rating to delete",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        try {
            this.ratingsService.deleteRating(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            this.logger.severe(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
