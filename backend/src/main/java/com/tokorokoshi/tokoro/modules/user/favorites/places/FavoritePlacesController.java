package com.tokorokoshi.tokoro.modules.user.favorites.places;

import com.tokorokoshi.tokoro.modules.exceptions.establishments.InvalidEstablishmentException;
import com.tokorokoshi.tokoro.modules.places.dto.PlaceDto;
import com.tokorokoshi.tokoro.modules.user.favorites.places.dto.FavoritePlaceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

import java.util.List;

/**
 * REST controller for managing favorite places for users.
 * This controller provides endpoints to add, update, remove, and retrieve favorite places.
 */
@Tag(
        name = "Favorite Places",
        description = "API for managing favorite places for users"
)
@RestController
@RequestMapping("/api/users/favorite-places")
public class FavoritePlacesController {
    private static final Logger log
            = LoggerFactory.getLogger(FavoritePlacesController.class);

    private final FavoritePlacesService favoritePlacesService;

    @Autowired
    public FavoritePlacesController(FavoritePlacesService favoritePlacesService) {
        this.favoritePlacesService = favoritePlacesService;
    }

    /**
     * Adds a favorite place to the currently authenticated user's metadata.
     *
     * @param favoritePlaceDto the favorite place to add.
     * @return a response entity indicating success or failure.
     */
    @Operation(
            summary = "Add a favorite place",
            description = "Accepts a request with a JSON body to add a favorite place"
    )
    @PostMapping
    public ResponseEntity<String> addFavoritePlace(
            @Parameter(
                    description = "The favorite place to add",
                    required = true,
                    example = "{\"establishmentId\": \"60f1b3b3b3b3b3b3b3b3b3b3\"}"
            )
            @Valid
            @RequestBody
            FavoritePlaceDto favoritePlaceDto
    ) {
        try {
            favoritePlacesService.addFavoritePlace(favoritePlaceDto);
            return ResponseEntity.ok("Favorite place added successfully");
        } catch (InvalidEstablishmentException e) {
            log.error(
                    "Error adding favorite place because of invalid establishment",
                    e
            );
            return ResponseEntity
                    .badRequest()
                    .body("Invalid establishment");
        } catch (Exception e) {
            log.error("Error adding favorite place", e);
            return ResponseEntity
                    .internalServerError()
                    .body("Failed to add favorite place");
        }
    }

    /**
     * Updates a favorite place in the currently authenticated user's metadata.
     *
     * @param establishmentId  the establishment ID of the favorite place to update.
     * @param favoritePlaceDto the updated favorite place.
     * @return a response entity indicating success or failure.
     */
    @Operation(
            summary = "Update a favorite place",
            description = "Accepts a request with a JSON body to update a favorite place"
    )
    @PutMapping("/{establishmentId}")
    public ResponseEntity<String> updateFavoritePlace(
            @Parameter(
                    description = "The establishment ID of the favorite place to update",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            @NotNull
            @NotBlank
            String establishmentId,
            @Valid
            @RequestBody
            FavoritePlaceDto favoritePlaceDto
    ) {
        try {
            favoritePlacesService.updateFavoritePlace(
                    establishmentId,
                    favoritePlaceDto
            );
            return ResponseEntity.ok("Favorite place updated successfully");
        } catch (InvalidEstablishmentException e) {
            log.error(
                    "Error updating favorite place because of invalid establishment",
                    e
            );
            return ResponseEntity
                    .badRequest()
                    .body("Invalid establishment ID");
        } catch (Exception e) {
            log.error("Error updating favorite place", e);
            return ResponseEntity
                    .internalServerError()
                    .body("Failed to update favorite place");
        }
    }

    /**
     * Removes a favorite place from the currently authenticated user's metadata.
     *
     * @param establishmentId the establishment ID of the favorite place to remove.
     * @return a response entity indicating success or failure.
     */
    @Operation(
            summary = "Remove a favorite place",
            description = "Accepts a request with a path variable to remove a favorite place"
    )
    @DeleteMapping("/{establishmentId}")
    public ResponseEntity<String> removeFavoritePlace(
            @Parameter(
                    description = "The establishment ID of the favorite place to remove",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            @NotNull
            @NotBlank
            String establishmentId
    ) {
        try {
            favoritePlacesService.removeFavoritePlace(establishmentId);
            return ResponseEntity.ok("Favorite place removed successfully");
        } catch (InvalidEstablishmentException e) {
            log.error(
                    "Error removing favorite place because of invalid establishment",
                    e
            );
            return ResponseEntity
                    .badRequest()
                    .body("Invalid establishment ID");
        } catch (Exception e) {
            log.error("Error removing favorite place", e);
            return ResponseEntity
                    .internalServerError()
                    .body("Failed to remove favorite place");
        }
    }

    /**
     * Retrieves all favorite places for the currently authenticated user as PlaceDto objects.
     *
     * @return a response entity containing a list of {@link PlaceDto} objects.
     */
    @Operation(
            summary = "Get all favorite places",
            description = "Returns a list of all favorite places"
    )
    @GetMapping
    public ResponseEntity<List<PlaceDto>> getFavoritePlaces() {
        try {
            List<PlaceDto> favoritePlaces = favoritePlacesService
                    .getFavoritePlaces();
            return ResponseEntity.ok(favoritePlaces);
        } catch (Exception e) {
            log.error("Error retrieving favorite places", e);
            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }

    /**
     * Retrieves a specific favorite place by its establishment ID as a PlaceDto object.
     *
     * @param establishmentId the establishment ID of the favorite place to retrieve.
     * @return a response entity containing the {@link PlaceDto} object if found, otherwise a not found response.
     */
    @Operation(
            summary = "Get a favorite place by ID",
            description = "Returns the favorite place with the given establishment ID"
    )
    @GetMapping("/{establishmentId}")
    public ResponseEntity<PlaceDto> getFavoritePlaceById(
            @Parameter(
                    description = "The establishment ID of the favorite place to retrieve",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            @NotNull
            @NotBlank
            String establishmentId
    ) throws BadRequestException {
        try {
            PlaceDto favoritePlace = favoritePlacesService
                    .getFavoritePlaceById(establishmentId);
            if (favoritePlace != null) {
                return ResponseEntity.ok(favoritePlace);
            } else {
                throw new BadRequestException("Favorite place not found");
            }
        } catch (InvalidEstablishmentException ex) {
            throw new BadRequestException("Invalid establishment ID");
        } catch (Exception ex) {
            throw new ServerErrorException(ex.getMessage(), ex);
        }
    }

    /**
     * Clears all favorite places for the currently authenticated user.
     *
     * @return a response entity indicating success or failure.
     */
    @Operation(
            summary = "Clear all favorite places",
            description = "Removes all favorite places"
    )
    @DeleteMapping
    public ResponseEntity<String> clearFavoritePlaces() {
        try {
            favoritePlacesService.clearFavoritePlaces();
            return ResponseEntity.ok("All favorite places cleared successfully");
        } catch (Exception e) {
            log.error("Error clearing favorite places", e);
            return ResponseEntity
                    .internalServerError()
                    .body("Failed to clear favorite places");
        }
    }

    /**
     * Checks if a place is already a favorite for the currently authenticated user.
     *
     * @param establishmentId the establishment ID to check.
     * @return a response entity indicating whether the place is a favorite.
     */
    @Operation(
            summary = "Check if a place is a favorite",
            description = "Returns a boolean indicating whether the place is a favorite"
    )
    @GetMapping("/check/{establishmentId}")
    public ResponseEntity<Boolean> isFavoritePlace(
            @Parameter(
                    description = "The establishment ID of the place to check",
                    required = true
            )
            @PathVariable
            @NotNull
            @NotBlank
            String establishmentId
    ) {
        try {
            boolean isFavorite = favoritePlacesService
                    .isFavoritePlace(establishmentId);
            return ResponseEntity.ok(isFavorite);
        } catch (InvalidEstablishmentException e) {
            log.error(
                    "Error checking favorite place because of invalid establishment",
                    e
            );
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error checking if place is a favorite", e);
            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }

    /**
     * Sorts favorite places by the added date in ascending order.
     *
     * @return a response entity containing a sorted list of {@link PlaceDto} objects.
     */
    @Operation(
            summary = "Sort favorite places by date",
            description = "Returns a list of favorite places sorted by the added date"
    )
    @GetMapping("/sort/date")
    public ResponseEntity<List<PlaceDto>> getFavoritePlacesSortedByDate() {
        try {
            List<PlaceDto> favoritePlaces = favoritePlacesService
                    .getFavoritePlacesSortedByDate();
            return ResponseEntity.ok(favoritePlaces);
        } catch (Exception e) {
            log.error("Error sorting favorite places by date", e);
            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }

    /**
     * Sorts favorite places by the added date in descending order.
     *
     * @return a response entity containing a sorted list of {@link PlaceDto} objects.
     */
    @Operation(
            summary = "Sort favorite places by date in descending order",
            description = "Returns a list of favorite places sorted by the added date in descending order"
    )
    @GetMapping("/sort/date-desc")
    public ResponseEntity<List<PlaceDto>> getFavoritePlacesSortedByDateDescending() {
        try {
            List<PlaceDto> favoritePlaces = favoritePlacesService
                    .getFavoritePlacesSortedByDateDescending();
            return ResponseEntity.ok(favoritePlaces);
        } catch (Exception e) {
            log.error(
                    "Error sorting favorite places by date in descending order",
                    e
            );
            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }

    /**
     * Sorts favorite places by the rating in descending order.
     *
     * @return a response entity containing a sorted list of {@link PlaceDto} objects.
     */
    @Operation(
            summary = "Sort favorite places by rating",
            description = "Returns a list of favorite places sorted by the rating"
    )
    @GetMapping("/sort/rating")
    public ResponseEntity<List<PlaceDto>> getFavoritePlacesSortedByRating() {
        try {
            List<PlaceDto> favoritePlaces = favoritePlacesService
                    .getFavoritePlacesSortedByRating();
            return ResponseEntity.ok(favoritePlaces);
        } catch (Exception e) {
            log.error("Error sorting favorite places by rating", e);
            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }

    /**
     * Searches favorite places by name.
     *
     * @param name the name to search for.
     * @return a response entity containing a list of {@link PlaceDto} objects that match the search criteria.
     */
    @Operation(
            summary = "Search favorite places by name",
            description = "Returns a list of favorite places that match the search criteria"
    )
    @GetMapping("/search")
    public ResponseEntity<List<PlaceDto>> searchFavoritePlacesByName(
            @Parameter(
                    description = "The name to search for",
                    required = true,
                    example = "Tokoro Sushi"
            )
            @RequestParam
            @NotNull
            @NotBlank
            String name
    ) {
        try {
            List<PlaceDto> favoritePlaces =
                    favoritePlacesService.searchFavoritePlacesByName(name);
            return ResponseEntity.ok(favoritePlaces);
        } catch (InvalidEstablishmentException e) {
            log.error(
                    "Error searching favorite place because of invalid establishment",
                    e
            );
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            throw new ServerErrorException(ex.getMessage(), ex);
        }
    }

    /**
     * Counts the number of favorite places for the currently authenticated user.
     *
     * @return a response entity containing the number of favorite places.
     */
    @Operation(
            summary = "Count favorite places",
            description = "Returns the number of favorite places"
    )
    @GetMapping("/count")
    public ResponseEntity<Integer> countFavoritePlaces() {
        try {
            int count = favoritePlacesService.countFavoritePlaces();
            return ResponseEntity.ok(count);
        } catch (Exception ex) {
            throw new ServerErrorException(ex.getMessage(), ex);
        }
    }

    /**
     * Rolls back the last favorite place added for the currently authenticated user.
     *
     * @return a response entity indicating success or failure.
     */
    @Operation(
            summary = "Roll back last favorite place",
            description = "Rolls back the last favorite place added"
    )
    @DeleteMapping("/rollback")
    public ResponseEntity<String> rollbackLastFavoritePlace() {
        try {
            favoritePlacesService.rollbackLastFavoritePlace();
            return ResponseEntity.ok(
                    "Last favorite place rolled back successfully");
        } catch (Exception ex) {
            throw new ServerErrorException(
                    "Failed to roll back last favorite place",
                    ex
            );
        }
    }
}