package com.tokorokoshi.tokoro.modules.user.favorites.places;

import com.tokorokoshi.tokoro.modules.exceptions.establishments.InvalidEstablishmentException;
import com.tokorokoshi.tokoro.modules.places.dto.PlaceDto;
import com.tokorokoshi.tokoro.modules.user.favorites.places.dto.FavoritePlaceDto;
import com.tokorokoshi.tokoro.modules.exceptions.favorites.places.FavoritePlaceNotFoundException;
import com.tokorokoshi.tokoro.modules.exceptions.favorites.places.NoFavoritePlacesException;
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
        if (favoritePlacesService == null) {
            throw new IllegalArgumentException("FavoritePlacesService must not be null");
        }
        this.favoritePlacesService = favoritePlacesService;
    }

    /**
     * Adds a favorite place to the currently authenticated user's metadata.
     *
     * @param favoritePlaceDto the favorite place to add.
     * @return a response entity indicating success or failure.
     * @throws InvalidEstablishmentException if the favoritePlaceDto is invalid.
     */
    @PostMapping
    public ResponseEntity<String> addFavoritePlace(@Valid @RequestBody FavoritePlaceDto favoritePlaceDto) {
        try {
            log.debug("Adding favorite place: {}", favoritePlaceDto);
            favoritePlacesService.addFavoritePlace(favoritePlaceDto);
            log.info("Successfully added favorite place with establishment ID: {}", favoritePlaceDto.establishmentId());
            return ResponseEntity.ok("Favorite place added successfully");
        } catch (InvalidEstablishmentException e) {
            log.error("Invalid establishment provided", e);
            return ResponseEntity.badRequest().body("Invalid establishment: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error adding favorite place", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add favorite place: " + e.getMessage());
        }
    }

    /**
     * Updates a favorite place in the currently authenticated user's metadata.
     *
     * @param establishmentId the establishment ID of the favorite place to update.
     * @param favoritePlaceDto the updated favorite place.
     * @return a response entity indicating success or failure.
     * @throws InvalidEstablishmentException if the establishmentId or favoritePlaceDto is invalid.
     * @throws FavoritePlaceNotFoundException if the favorite place with the specified establishment ID does not exist.
     */
    @PutMapping("/{establishmentId}")
    public ResponseEntity<String> updateFavoritePlace(
            @PathVariable @NotNull @NotBlank String establishmentId,
            @Valid @RequestBody FavoritePlaceDto favoritePlaceDto) {
        try {
            log.debug("Updating favorite place with establishment ID: {}", establishmentId);
            favoritePlacesService.updateFavoritePlace(establishmentId, favoritePlaceDto);
            log.info("Successfully updated favorite place with establishment ID: {}", establishmentId);
            return ResponseEntity.ok("Favorite place updated successfully");
        } catch (InvalidEstablishmentException e) {
            log.error("Invalid establishment ID: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid establishment ID: " + e.getMessage());
        } catch (FavoritePlaceNotFoundException e) {
            log.warn("Favorite place with establishment ID {} not found", establishmentId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Favorite place with establishment ID " + establishmentId + " not found");
        } catch (Exception e) {
            log.error("Error updating favorite place", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update favorite place: " + e.getMessage());
        }
    }

    /**
     * Removes a favorite place from the currently authenticated user's metadata.
     *
     * @param establishmentId the establishment ID of the favorite place to remove.
     * @return a response entity indicating success or failure.
     * @throws InvalidEstablishmentException if the establishmentId is invalid.
     * @throws FavoritePlaceNotFoundException if the favorite place with the specified establishment ID does not exist.
     */
    @DeleteMapping("/{establishmentId}")
    public ResponseEntity<String> removeFavoritePlace(
            @PathVariable @NotNull @NotBlank String establishmentId) {
        try {
            log.debug("Removing favorite place with establishment ID: {}", establishmentId);
            favoritePlacesService.removeFavoritePlace(establishmentId);
            log.info("Successfully removed favorite place with establishment ID: {}", establishmentId);
            return ResponseEntity.ok("Favorite place removed successfully");
        } catch (InvalidEstablishmentException e) {
            log.error("Invalid establishment ID: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid establishment ID: " + e.getMessage());
        } catch (FavoritePlaceNotFoundException e) {
            log.warn("Favorite place with establishment ID {} not found", establishmentId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Favorite place with establishment ID " + establishmentId + " not found");
        } catch (Exception e) {
            log.error("Error removing favorite place", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to remove favorite place: " + e.getMessage());
        }
    }

    /**
     * Retrieves all favorite places for the currently authenticated user as PlaceDto objects.
     *
     * @return a response entity containing a list of {@link PlaceDto} objects.
     * @throws NoFavoritePlacesException if there are no favorite places.
     */
    @GetMapping
    public ResponseEntity<List<PlaceDto>> getFavoritePlaces() {
        try {
            log.debug("Retrieving all favorite places");
            List<PlaceDto> favoritePlaces = favoritePlacesService.getFavoritePlaces();
            log.info("Successfully retrieved {} favorite places", favoritePlaces.size());
            return ResponseEntity.ok(favoritePlaces);
        } catch (NoFavoritePlacesException e) {
            log.warn("No favorite places found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(List.of()); // Return an empty list
        } catch (Exception e) {
            log.error("Error retrieving favorite places", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of()); // Return an empty list
        }
    }

    /**
     * Retrieves a specific favorite place by its establishment ID as a PlaceDto object.
     *
     * @param establishmentId the establishment ID of the favorite place to retrieve.
     * @return a response entity containing the {@link PlaceDto} object if found, otherwise a not found response.
     * @throws InvalidEstablishmentException if the establishmentId is invalid.
     * @throws FavoritePlaceNotFoundException if the favorite place with the specified establishment ID does not exist.
     */
    @GetMapping("/{establishmentId}")
    public ResponseEntity<PlaceDto> getFavoritePlaceById(
            @PathVariable @NotNull @NotBlank String establishmentId) {
        try {
            log.debug("Retrieving favorite place with establishment ID: {}", establishmentId);
            PlaceDto favoritePlace = favoritePlacesService.getFavoritePlaceById(establishmentId);
            if (favoritePlace != null) {
                log.info("Successfully retrieved favorite place with establishment ID: {}", establishmentId);
                return ResponseEntity.ok(favoritePlace);
            } else {
                log.warn("Favorite place with establishment ID {} not found", establishmentId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null); // Return null for not found
            }
        } catch (InvalidEstablishmentException e) {
            log.error("Invalid establishment ID: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null); // Return null for bad request
        } catch (FavoritePlaceNotFoundException e) {
            log.warn("Favorite place with establishment ID {} not found", establishmentId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Return null for not found
        } catch (Exception e) {
            log.error("Error retrieving favorite place by ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Return null for internal server error
        }
    }

    /**
     * Clears all favorite places for the currently authenticated user.
     *
     * @return a response entity indicating success or failure.
     * @throws NoFavoritePlacesException if there are no favorite places to clear.
     */
    @DeleteMapping
    public ResponseEntity<String> clearFavoritePlaces() {
        try {
            log.debug("Clearing all favorite places");
            favoritePlacesService.clearFavoritePlaces();
            log.info("Successfully cleared all favorite places");
            return ResponseEntity.ok("All favorite places cleared successfully");
        } catch (NoFavoritePlacesException e) {
            log.warn("No favorite places to clear");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No favorite places to clear");
        } catch (Exception e) {
            log.error("Error clearing favorite places", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to clear favorite places: " + e.getMessage());
        }
    }

    /**
     * Checks if a place is already a favorite for the currently authenticated user.
     *
     * @param establishmentId the establishment ID to check.
     * @return a response entity indicating whether the place is a favorite.
     * @throws InvalidEstablishmentException if the establishmentId is invalid.
     */
    @GetMapping("/check/{establishmentId}")
    public ResponseEntity<Boolean> isFavoritePlace(
            @PathVariable @NotNull @NotBlank String establishmentId) {
        try {
            log.debug("Checking if establishment ID {} is a favorite", establishmentId);
            boolean isFavorite = favoritePlacesService.isFavoritePlace(establishmentId);
            log.info("Establishment ID {} is favorite: {}", establishmentId, isFavorite);
            return ResponseEntity.ok(isFavorite);
        } catch (InvalidEstablishmentException e) {
            log.error("Invalid establishment ID: {}", e.getMessage());
            return ResponseEntity.badRequest().body(false); // Return false for bad request
        } catch (Exception e) {
            log.error("Error checking if place is a favorite", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(false); // Return false for internal server error
        }
    }

    /**
     * Sorts favorite places by the added date in ascending order.
     *
     * @return a response entity containing a sorted list of {@link PlaceDto} objects.
     * @throws NoFavoritePlacesException if there are no favorite places.
     */
    @GetMapping("/sort/date")
    public ResponseEntity<List<PlaceDto>> getFavoritePlacesSortedByDate() {
        try {
            log.debug("Sorting favorite places by date");
            List<PlaceDto> favoritePlaces = favoritePlacesService.getFavoritePlacesSortedByDate();
            log.info("Successfully sorted {} favorite places by date", favoritePlaces.size());
            return ResponseEntity.ok(favoritePlaces);
        } catch (NoFavoritePlacesException e) {
            log.warn("No favorite places found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(List.of()); // Return an empty list
        } catch (Exception e) {
            log.error("Error sorting favorite places by date", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of()); // Return an empty list
        }
    }

    /**
     * Sorts favorite places by the added date in descending order.
     *
     * @return a response entity containing a sorted list of {@link PlaceDto} objects.
     * @throws NoFavoritePlacesException if there are no favorite places.
     */
    @GetMapping("/sort/date-desc")
    public ResponseEntity<List<PlaceDto>> getFavoritePlacesSortedByDateDescending() {
        try {
            log.debug("Sorting favorite places by date in descending order");
            List<PlaceDto> favoritePlaces = favoritePlacesService.getFavoritePlacesSortedByDateDescending();
            log.info("Successfully sorted {} favorite places by date in descending order", favoritePlaces.size());
            return ResponseEntity.ok(favoritePlaces);
        } catch (NoFavoritePlacesException e) {
            log.warn("No favorite places found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(List.of()); // Return an empty list
        } catch (Exception e) {
            log.error("Error sorting favorite places by date in descending order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of()); // Return an empty list
        }
    }

    /**
     * Sorts favorite places by the rating in descending order.
     *
     * @return a response entity containing a sorted list of {@link PlaceDto} objects.
     * @throws NoFavoritePlacesException if there are no favorite places.
     */
    @GetMapping("/sort/rating")
    public ResponseEntity<List<PlaceDto>> getFavoritePlacesSortedByRating() {
        try {
            log.debug("Sorting favorite places by rating");
            List<PlaceDto> favoritePlaces = favoritePlacesService.getFavoritePlacesSortedByRating();
            log.info("Successfully sorted {} favorite places by rating", favoritePlaces.size());
            return ResponseEntity.ok(favoritePlaces);
        } catch (NoFavoritePlacesException e) {
            log.warn("No favorite places found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(List.of()); // Return an empty list
        } catch (Exception e) {
            log.error("Error sorting favorite places by rating", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of()); // Return an empty list
        }
    }

    /**
     * Searches favorite places by name.
     *
     * @param name the name to search for.
     * @return a response entity containing a list of {@link PlaceDto} objects that match the search criteria.
     * @throws InvalidEstablishmentException if the name is invalid.
     */
    @GetMapping("/search")
    public ResponseEntity<List<PlaceDto>> searchFavoritePlacesByName(
            @RequestParam @NotNull @NotBlank String name) {
        try {
            log.debug("Searching favorite places by name: {}", name);
            List<PlaceDto> favoritePlaces = favoritePlacesService.searchFavoritePlacesByName(name);
            log.info("Successfully searched favorite places by name '{}': found {} results", name, favoritePlaces.size());
            return ResponseEntity.ok(favoritePlaces);
        } catch (InvalidEstablishmentException e) {
            log.error("Invalid name provided", e);
            return ResponseEntity.badRequest().body(List.of()); // Return an empty list for bad request
        } catch (Exception e) {
            log.error("Error searching favorite places by name", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of()); // Return an empty list for internal server error
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
            log.debug("Counting favorite places");
            int count = favoritePlacesService.countFavoritePlaces();
            log.info("Successfully counted favorite places: {}", count);
            return ResponseEntity.ok(count);
        } catch (NoFavoritePlacesException e) {
            log.warn("No favorite places found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(0); // Return 0 for not found
        } catch (Exception e) {
            log.error("Error counting favorite places", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(0); // Return 0 for internal server error
        }
    }

    /**
     * Rolls back the last favorite place added for the currently authenticated user.
     *
     * @return a response entity indicating success or failure.
     * @throws NoFavoritePlacesException if there are no favorite places to rollback.
     */
    @DeleteMapping("/rollback")
    public ResponseEntity<String> rollbackLastFavoritePlace() {
        try {
            log.debug("Rolling back the last favorite place");
            favoritePlacesService.rollbackLastFavoritePlace();
            log.info("Successfully rolled back the last favorite place");
            return ResponseEntity.ok("Last favorite place rolled back successfully");
        } catch (NoFavoritePlacesException e) {
            log.warn("No favorite places to rollback");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No favorite places to rollback");
        } catch (Exception e) {
            log.error("Error rolling back last favorite place", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to roll back last favorite place: " + e.getMessage());
        }
    }
}