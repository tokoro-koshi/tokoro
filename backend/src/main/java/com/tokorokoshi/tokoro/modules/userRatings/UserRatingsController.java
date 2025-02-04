package com.tokorokoshi.tokoro.modules.userRatings;

import com.tokorokoshi.tokoro.modules.userRatings.dto.CreateUpdateUserRatingDto;
import com.tokorokoshi.tokoro.modules.userRatings.dto.UserRatingDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "User Ratings", description = "API for managing user ratings")
@RestController
@RequestMapping("/api/user-ratings")
public class UserRatingsController {
    private final UserRatingsService userRatingsService;

    @Autowired
    public UserRatingsController(UserRatingsService userRatingsService) {
        this.userRatingsService = userRatingsService;
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
    public ResponseEntity<UserRatingDto> createUserRating(
            @Parameter(
                    description = "The user rating to create",
                    required = true
            )
            @RequestBody
            CreateUpdateUserRatingDto userRating
    ) {
        return ResponseEntity.ok(
                this.userRatingsService.saveUserRating(userRating)
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
    public ResponseEntity<UserRatingDto> getUserRating(
            @Parameter(
                    description = "The ID of the user rating to get",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        return ResponseEntity.ok(
                this.userRatingsService.findUserRatingById(id)
        );
    }

    @Operation(
            summary = "Get all user ratings",
            description = "Returns a paginated list of all user ratings"
    )
    @GetMapping(
            value = {"", "/"},
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Page<UserRatingDto>> getAllUserRatings(
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
                this.userRatingsService.findAllUserRatings(pageable)
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
    public ResponseEntity<UserRatingDto> updateUserRating(
            @Parameter(
                    description = "The user rating to update",
                    required = true
            )
            @RequestBody
            CreateUpdateUserRatingDto userRating,
            @Parameter(
                    description = "The ID of the user rating to update",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        UserRatingDto updatedUserRating = this.userRatingsService
                .updateUserRating(id, userRating);
        if (updatedUserRating == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUserRating);
    }

    @Operation(
            summary = "Delete a user rating",
            description = "Deletes the user rating with the given ID"
    )
    @DeleteMapping(value = "/{id}")
    public void deleteUserRating(
            @Parameter(
                    description = "The ID of the user rating to delete",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b"
            )
            @PathVariable
            String id
    ) {
        this.userRatingsService.deleteUserRating(id);
    }
}
