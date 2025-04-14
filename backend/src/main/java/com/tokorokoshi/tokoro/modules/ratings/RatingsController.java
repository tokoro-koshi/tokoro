package com.tokorokoshi.tokoro.modules.ratings;

import com.tokorokoshi.tokoro.helpers.PaginationDto;
import com.tokorokoshi.tokoro.modules.error.NotFoundException;
import com.tokorokoshi.tokoro.modules.ratings.dto.CreateUpdateRatingDto;
import com.tokorokoshi.tokoro.modules.ratings.dto.RatingDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(
        name = "Places Ratings",
        description = "API for managing ratings of places"
)
@RestController
@RequestMapping("/ratings")
public class RatingsController {
    private final RatingsService ratingsService;
    private final Logger logger;
    private final PagedResourcesAssembler<RatingDto> pagedResourcesAssembler;

    @Autowired
    public RatingsController(
            RatingsService ratingsService,
            PagedResourcesAssembler<RatingDto> pagedResourcesAssembler
    ) {
        this.ratingsService = ratingsService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.logger = Logger.getLogger(RatingsController.class.getName());
    }

    @Operation(
            summary = "Create a new rating",
            description = "Accepts a request with a JSON body to create a new rating, and returns the created rating"
    )
    @PostMapping(
            value = {"", "/"},
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<RatingDto> create(
            @Parameter(
                    description = "The rating to create",
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
            summary = "Get a rating by ID",
            description = "Returns the rating with the given ID"
    )
    @GetMapping(
            value = "/{id}",
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<RatingDto> get(
            @Parameter(
                    description = "The ID of the rating to get",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        var rating = this.ratingsService.findRatingById(id);
        if (rating == null) {
            throw new NotFoundException("Rating not found");
        }
        return ResponseEntity.ok(rating);
    }

    @Operation(
            summary = "Get all ratings",
            description = "Returns a paginated list of all ratings"
    )
    @GetMapping(
            value = {"", "/"},
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PaginationDto<RatingDto>> getAllRatings(
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
            int size,
            @Parameter(
                    description = "The user ID to filter by",
                    example = "60f1b3b3b3b3b3b3b3b3b3b"
            )
            @RequestParam(defaultValue = "null")
            String userId,
            @Parameter(
                    description = "The place ID to filter by",
                    example = "60f1b3b3b3b3b3b3b3b3b3b"
            )
            @RequestParam(defaultValue = "null")
            String placeId
    ) {
        Pageable pageable = PageRequest.of(page, size);
        var ratings = this.ratingsService.findAllRatings(
                pageable,
                userId,
                placeId
        );
        var pagination = PaginationDto.fromEntityModel(
                this.pagedResourcesAssembler.toModel(ratings)
        );
        return ResponseEntity.ok(pagination);
    }

    @Operation(
            summary = "Update a rating",
            description = "Accepts a request with a JSON body to update a rating, and returns the updated rating"
    )
    @PutMapping(
            value = "/{id}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<RatingDto> update(
            @Parameter(
                    description = "The rating to update",
                    required = true
            )
            @RequestBody
            CreateUpdateRatingDto rating,
            @Parameter(
                    description = "The ID of the rating to update",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        RatingDto updatedRating = this.ratingsService.updateRating(id, rating);
        if (updatedRating == null) {
            throw new NotFoundException("Rating not found");
        }
        return ResponseEntity.ok(updatedRating);
    }

    @Operation(
            summary = "Delete a rating",
            description = "Deletes the rating with the given ID"
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(
            @Parameter(
                    description = "The ID of the rating to delete",
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
            throw new NotFoundException("Rating not found");
        } catch (Exception e) {
            this.logger.severe(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
