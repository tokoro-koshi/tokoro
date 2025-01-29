package com.tokorokoshi.tokoro.modules.user.favorites.places;

import com.tokorokoshi.tokoro.modules.exceptions.establishments.InvalidEstablishmentException;
import com.tokorokoshi.tokoro.modules.places.dto.PlaceDto;
import com.tokorokoshi.tokoro.modules.user.favorites.places.dto.FavoritePlaceDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing favorite places for users.
 * This controller provides endpoints to add, update, remove, and retrieve favorite places.
 */
@RestController
@RequestMapping("/api/v1/users/favorite-places")
public class FavoritePlacesController {

    private static final Logger log = LoggerFactory.getLogger(FavoritePlacesController.class);

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
    @PostMapping
    public ResponseEntity<String> addFavoritePlace(@Valid @RequestBody FavoritePlaceDto favoritePlaceDto) {
        try {
            favoritePlacesService.addFavoritePlace(favoritePlaceDto);
            return ResponseEntity.ok("Favorite place added successfully");
        } catch (InvalidEstablishmentException e) {
            log.error("Error adding favorite place because of invalid establishment", e);
            return ResponseEntity.badRequest().body("Invalid establishment");
        } catch (Exception e) {
            log.error("Error adding favorite place", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add favorite place");
        }
    }

    /**
     * Updates a favorite place in the currently authenticated user's metadata.
     *
     * @param establishmentId the establishment ID of the favorite place to update.
     * @param favoritePlaceDto the updated favorite place.
     * @return a response entity indicating success or failure.
     */
    @PutMapping("/{establishmentId}")
    public ResponseEntity<String> updateFavoritePlace(
            @PathVariable @NotNull @NotBlank String establishmentId,
            @Valid @RequestBody FavoritePlaceDto favoritePlaceDto) {
        try {
            favoritePlacesService.updateFavoritePlace(establishmentId, favoritePlaceDto);
            return ResponseEntity.ok("Favorite place updated successfully");
        } catch (InvalidEstablishmentException e) {
            log.error("Error updating favorite place because of invalid establishment", e);
            return ResponseEntity.badRequest().body("Invalid establishment ID");
        } catch (Exception e) {
            log.error("Error updating favorite place", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update favorite place");
        }
    }

    /**
     * Removes a favorite place from the currently authenticated user's metadata.
     *
     * @param establishmentId the establishment ID of the favorite place to remove.
     * @return a response entity indicating success or failure.
     */
    @DeleteMapping("/{establishmentId}")
    public ResponseEntity<String> removeFavoritePlace(
            @PathVariable @NotNull @NotBlank String establishmentId) {
        try {
            favoritePlacesService.removeFavoritePlace(establishmentId);
            return ResponseEntity.ok("Favorite place removed successfully");
        } catch (InvalidEstablishmentException e) {
            log.error("Error removing favorite place because of invalid establishment", e);
            return ResponseEntity.badRequest().body("Invalid establishment ID");
        } catch (Exception e) {
            log.error("Error removing favorite place", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to remove favorite place");
        }
    }

    /**
     * Retrieves all favorite places for the currently authenticated user as PlaceDto objects.
     *
     * @return a response entity containing a list of {@link PlaceDto} objects.
     */
    @GetMapping
    public ResponseEntity<List<PlaceDto>> getFavoritePlaces() {
        try {
            List<PlaceDto> favoritePlaces = favoritePlacesService.getFavoritePlaces();
            return ResponseEntity.ok(favoritePlaces);
        } catch (Exception e) {
            log.error("Error retrieving favorite places", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a specific favorite place by its establishment ID as a PlaceDto object.
     *
     * @param establishmentId the establishment ID of the favorite place to retrieve.
     * @return a response entity containing the {@link PlaceDto} object if found, otherwise a not found response.
     */
    @GetMapping("/{establishmentId}")
    public ResponseEntity<PlaceDto> getFavoritePlaceById(
            @PathVariable @NotNull @NotBlank String establishmentId) {
        try {
            PlaceDto favoritePlace = favoritePlacesService.getFavoritePlaceById(establishmentId);
            if (favoritePlace != null) {
                return ResponseEntity.ok(favoritePlace);
            } else {
                log.error("Favorite place with establishment ID {} not found", establishmentId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (InvalidEstablishmentException e) {
            log.error("Error retrieving favorite place because of invalid establishment", e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error retrieving favorite place by ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Clears all favorite places for the currently authenticated user.
     *
     * @return a response entity indicating success or failure.
     */
    @DeleteMapping
    public ResponseEntity<String> clearFavoritePlaces() {
        try {
            favoritePlacesService.clearFavoritePlaces();
            return ResponseEntity.ok("All favorite places cleared successfully");
        } catch (Exception e) {
            log.error("Error clearing favorite places", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to clear favorite places");
        }
    }

    /**
     * Checks if a place is already a favorite for the currently authenticated user.
     *
     * @param establishmentId the establishment ID to check.
     * @return a response entity indicating whether the place is a favorite.
     */
    @GetMapping("/check/{establishmentId}")
    public ResponseEntity<Boolean> isFavoritePlace(
            @PathVariable @NotNull @NotBlank String establishmentId) {
        try {
            boolean isFavorite = favoritePlacesService.isFavoritePlace(establishmentId);
            return ResponseEntity.ok(isFavorite);
        } catch (InvalidEstablishmentException e) {
            log.error("Error checking favorite place because of invalid establishment", e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error checking if place is a favorite", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Sorts favorite places by the added date in ascending order.
     *
     * @return a response entity containing a sorted list of {@link PlaceDto} objects.
     */
    @GetMapping("/sort/date")
    public ResponseEntity<List<PlaceDto>> getFavoritePlacesSortedByDate() {
        try {
            List<PlaceDto> favoritePlaces = favoritePlacesService.getFavoritePlacesSortedByDate();
            return ResponseEntity.ok(favoritePlaces);
        } catch (Exception e) {
            log.error("Error sorting favorite places by date", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Sorts favorite places by the added date in descending order.
     *
     * @return a response entity containing a sorted list of {@link PlaceDto} objects.
     */
    @GetMapping("/sort/date-desc")
    public ResponseEntity<List<PlaceDto>> getFavoritePlacesSortedByDateDescending() {
        try {
            List<PlaceDto> favoritePlaces = favoritePlacesService.getFavoritePlacesSortedByDateDescending();
            return ResponseEntity.ok(favoritePlaces);
        } catch (Exception e) {
            log.error("Error sorting favorite places by date in descending order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Sorts favorite places by the rating in descending order.
     *
     * @return a response entity containing a sorted list of {@link PlaceDto} objects.
     */
    @GetMapping("/sort/rating")
    public ResponseEntity<List<PlaceDto>> getFavoritePlacesSortedByRating() {
        try {
            List<PlaceDto> favoritePlaces = favoritePlacesService.getFavoritePlacesSortedByRating();
            return ResponseEntity.ok(favoritePlaces);
        } catch (Exception e) {
            log.error("Error sorting favorite places by rating", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Searches favorite places by name.
     *
     * @param name the name to search for.
     * @return a response entity containing a list of {@link PlaceDto} objects that match the search criteria.
     */
    @GetMapping("/search")
    public ResponseEntity<List<PlaceDto>> searchFavoritePlacesByName(
            @RequestParam @NotNull @NotBlank String name) {
        try {
            List<PlaceDto> favoritePlaces = favoritePlacesService.searchFavoritePlacesByName(name);
            return ResponseEntity.ok(favoritePlaces);
        } catch (InvalidEstablishmentException e) {
            log.error("Error searching favorite place because of invalid establishment", e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error searching favorite places by name", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Counts the number of favorite places for the currently authenticated user.
     *
     * @return a response entity containing the number of favorite places.
     */
    @GetMapping("/count")
    public ResponseEntity<Integer> countFavoritePlaces() {
        try {
            int count = favoritePlacesService.countFavoritePlaces();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            log.error("Error counting favorite places", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Rolls back the last favorite place added for the currently authenticated user.
     *
     * @return a response entity indicating success or failure.
     */
    @DeleteMapping("/rollback")
    public ResponseEntity<String> rollbackLastFavoritePlace() {
        try {
            favoritePlacesService.rollbackLastFavoritePlace();
            return ResponseEntity.ok("Last favorite place rolled back successfully");
        } catch (Exception e) {
            log.error("Error rolling back last favorite place", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to roll back last favorite place");
        }
    }
}