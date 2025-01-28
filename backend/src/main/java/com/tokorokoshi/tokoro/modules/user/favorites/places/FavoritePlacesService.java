package com.tokorokoshi.tokoro.modules.user.favorites.places;

import com.auth0.json.mgmt.users.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tokorokoshi.tokoro.modules.auth0.Auth0UserDataService;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.Auth0ManagementException;
import com.tokorokoshi.tokoro.modules.exceptions.establishments.InvalidEstablishmentException;
import com.tokorokoshi.tokoro.modules.places.PlacesService;
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
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class responsible for managing favorite places for users.
 * This class handles operations such as adding, updating, removing, and retrieving favorite places.
 */
@Service
public class FavoritePlacesService {

    private static final Logger log = LoggerFactory.getLogger(FavoritePlacesService.class);

    private static final String FAVORITE_PLACES_KEY = "favorite_places";

    private final Auth0UserDataService auth0UserDataService;
    private final PlacesService placesService;
    private final ObjectMapper objectMapper;

    @Autowired
    public FavoritePlacesService(Auth0UserDataService auth0UserDataService, PlacesService placesService, ObjectMapper objectMapper) {
        if (auth0UserDataService == null) {
            throw new IllegalArgumentException("Auth0UserDataService must not be null");
        }
        if (placesService == null) {
            throw new IllegalArgumentException("PlacesService must not be null");
        }
        if (objectMapper == null) {
            throw new IllegalArgumentException("ObjectMapper must not be null");
        }
        this.auth0UserDataService = auth0UserDataService;
        this.placesService = placesService;
        this.objectMapper = objectMapper;
    }

    /**
     * Adds a favorite place to the currently authenticated user's metadata.
     *
     * @param favoritePlaceDto the favorite place to add.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     * @throws InvalidEstablishmentException if the establishment ID is invalid.
     */
    public void addFavoritePlace(@Valid FavoritePlaceDto favoritePlaceDto) throws Auth0ManagementException {
        validateEstablishmentId(favoritePlaceDto.establishmentId());

        User user = auth0UserDataService.getAuthenticatedUserDetails();
        String userId = user.getId();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces = getFavoritePlacesFromMetadata(userMetadata);
        if (favoritePlaces.stream().anyMatch(fp -> fp.establishmentId().equals(favoritePlaceDto.establishmentId()))) {
            log.warn("Favorite place with establishment ID {} already exists for user {}", favoritePlaceDto.establishmentId(), userId);
            return;
        }

        favoritePlaces.add(favoritePlaceDto);
        auth0UserDataService.updateAuthenticatedUserMetadata(Map.of(FAVORITE_PLACES_KEY, favoritePlaces));
        log.info("Added favorite place with establishment ID {} for user {}", favoritePlaceDto.establishmentId(), userId);
    }

    /**
     * Updates a favorite place in the currently authenticated user's metadata.
     *
     * @param establishmentId the establishment ID of the favorite place to update.
     * @param favoritePlaceDto the updated favorite place.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     * @throws InvalidEstablishmentException if the establishment ID is invalid.
     * @throws FavoritePlaceNotFoundException if the favorite place with the specified establishment ID does not exist.
     */
    public void updateFavoritePlace(@NotNull String establishmentId, @Valid FavoritePlaceDto favoritePlaceDto) throws Auth0ManagementException {
        validateEstablishmentId(favoritePlaceDto.establishmentId());

        User user = auth0UserDataService.getAuthenticatedUserDetails();
        String userId = user.getId();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces = getFavoritePlacesFromMetadata(userMetadata);
        if (favoritePlaces.stream().noneMatch(fp -> fp.establishmentId().equals(establishmentId))) {
            log.warn("Favorite place with establishment ID {} does not exist for user {}", establishmentId, userId);
            throw new FavoritePlaceNotFoundException("Favorite place with establishment ID " + establishmentId + " does not exist for user " + userId);
        }

        favoritePlaces = favoritePlaces.stream()
                .map(fp -> fp.establishmentId().equals(establishmentId) ? favoritePlaceDto : fp)
                .toList();

        auth0UserDataService.updateAuthenticatedUserMetadata(Map.of(FAVORITE_PLACES_KEY, favoritePlaces));
        log.info("Updated favorite place with establishment ID {} for user {}", establishmentId, userId);
    }

    /**
     * Removes a favorite place from the currently authenticated user's metadata.
     *
     * @param establishmentId the establishment ID of the favorite place to remove.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     * @throws InvalidEstablishmentException if the establishment ID is invalid.
     * @throws FavoritePlaceNotFoundException if the favorite place with the specified establishment ID does not exist.
     */
    public void removeFavoritePlace(@NotNull String establishmentId) throws Auth0ManagementException {
        validateEstablishmentId(establishmentId);

        User user = auth0UserDataService.getAuthenticatedUserDetails();
        String userId = user.getId();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces = getFavoritePlacesFromMetadata(userMetadata);
        if (favoritePlaces.stream().noneMatch(fp -> fp.establishmentId().equals(establishmentId))) {
            log.warn("Favorite place with establishment ID {} does not exist for user {}", establishmentId, userId);
            throw new FavoritePlaceNotFoundException("Favorite place with establishment ID " + establishmentId + " does not exist for user " + userId);
        }

        favoritePlaces = favoritePlaces.stream()
                .filter(fp -> !fp.establishmentId().equals(establishmentId))
                .toList();

        auth0UserDataService.updateAuthenticatedUserMetadata(Map.of(FAVORITE_PLACES_KEY, favoritePlaces));
        log.info("Removed favorite place with establishment ID {} for user {}", establishmentId, userId);
    }

    /**
     * Retrieves all favorite places for the currently authenticated user as PlaceDto objects.
     *
     * @return a list of {@link PlaceDto} objects.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     */
    public List<PlaceDto> getFavoritePlaces() throws Auth0ManagementException {
        User user = auth0UserDataService.getAuthenticatedUserDetails();
        String userId = user.getId();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces = getFavoritePlacesFromMetadata(userMetadata);
        List<PlaceDto> places = favoritePlaces.stream()
                .map(fp -> placesService.getPlaceById(fp.establishmentId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        log.debug("Retrieved {} favorite places for user {}", places.size(), userId);
        return places;
    }

    /**
     * Retrieves a specific favorite place by its establishment ID as a PlaceDto object.
     *
     * @param establishmentId the establishment ID of the favorite place to retrieve.
     * @return the {@link PlaceDto} object if found, otherwise null.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     * @throws InvalidEstablishmentException if the establishment ID is invalid.
     * @throws FavoritePlaceNotFoundException if the favorite place with the specified establishment ID does not exist.
     */
    public PlaceDto getFavoritePlaceById(@NotNull String establishmentId) throws Auth0ManagementException {
        validateEstablishmentId(establishmentId);

        User user = auth0UserDataService.getAuthenticatedUserDetails();
        String userId = user.getId();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces = getFavoritePlacesFromMetadata(userMetadata);
        Optional<FavoritePlaceDto> favoritePlace = favoritePlaces.stream()
                .filter(fp -> fp.establishmentId().equals(establishmentId))
                .findFirst();

        if (favoritePlace.isPresent()) {
            PlaceDto place = placesService.getPlaceById(favoritePlace.get().establishmentId());
            log.debug("Retrieved favorite place with establishment ID {} for user {}", establishmentId, userId);
            return place;
        }

        log.warn("Favorite place with establishment ID {} does not exist for user {}", establishmentId, userId);
        throw new FavoritePlaceNotFoundException("Favorite place with establishment ID " + establishmentId + " does not exist for user " + userId);
    }

    /**
     * Clears all favorite places for the currently authenticated user.
     *
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     */
    public void clearFavoritePlaces() throws Auth0ManagementException {
        User user = auth0UserDataService.getAuthenticatedUserDetails();
        String userId = user.getId();
        auth0UserDataService.updateAuthenticatedUserMetadata(Map.of(FAVORITE_PLACES_KEY, new ArrayList<>()));
        log.info("Cleared all favorite places for user {}", userId);
    }

    /**
     * Checks if a place is already a favorite for the currently authenticated user.
     *
     * @param establishmentId the establishment ID to check.
     * @return true if the place is a favorite, false otherwise.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     * @throws InvalidEstablishmentException if the establishment ID is invalid.
     */
    public boolean isFavoritePlace(@NotNull String establishmentId) throws Auth0ManagementException {
        validateEstablishmentId(establishmentId);

        User user = auth0UserDataService.getAuthenticatedUserDetails();
        String userId = user.getId();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces = getFavoritePlacesFromMetadata(userMetadata);
        boolean isFavorite = favoritePlaces.stream().anyMatch(fp -> fp.establishmentId().equals(establishmentId));

        log.debug("Checked if establishment ID {} is a favorite for user {}: {}", establishmentId, userId, isFavorite);
        return isFavorite;
    }

    /**
     * Sorts favorite places by the added date in ascending order.
     *
     * @return a sorted list of {@link PlaceDto} objects.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     */
    public List<PlaceDto> getFavoritePlacesSortedByDate() throws Auth0ManagementException {
        User user = auth0UserDataService.getAuthenticatedUserDetails();
        String userId = user.getId();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces = getFavoritePlacesFromMetadata(userMetadata);
        List<PlaceDto> places = favoritePlaces.stream()
                .sorted(Comparator.comparing(FavoritePlaceDto::addedAt))
                .map(fp -> placesService.getPlaceById(fp.establishmentId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        log.debug("Retrieved and sorted {} favorite places by date for user {}", places.size(), userId);
        return places;
    }

    /**
     * Sorts favorite places by the added date in descending order.
     *
     * @return a sorted list of {@link PlaceDto} objects.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     */
    public List<PlaceDto> getFavoritePlacesSortedByDateDescending() throws Auth0ManagementException {
        User user = auth0UserDataService.getAuthenticatedUserDetails();
        String userId = user.getId();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces = getFavoritePlacesFromMetadata(userMetadata);
        List<PlaceDto> places = favoritePlaces.stream()
                .sorted(Comparator.comparing(FavoritePlaceDto::addedAt).reversed())
                .map(fp -> placesService.getPlaceById(fp.establishmentId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        log.debug("Retrieved and sorted {} favorite places by date in descending order for user {}", places.size(), userId);
        return places;
    }

    /**
     * Sorts favorite places by the rating in descending order.
     *
     * @return a sorted list of {@link PlaceDto} objects.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     */
    public List<PlaceDto> getFavoritePlacesSortedByRating() throws Auth0ManagementException {
        User user = auth0UserDataService.getAuthenticatedUserDetails();
        String userId = user.getId();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces = getFavoritePlacesFromMetadata(userMetadata);
        List<PlaceDto> places = favoritePlaces.stream()
                .filter(fp -> fp.rating() != null)
                .sorted(Comparator.comparing(FavoritePlaceDto::rating).reversed())
                .map(fp -> placesService.getPlaceById(fp.establishmentId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        log.debug("Retrieved and sorted {} favorite places by rating for user {}", places.size(), userId);
        return places;
    }

    /**
     * Searches favorite places by name.
     *
     * @param name the name to search for.
     * @return a list of {@link PlaceDto} objects that match the search criteria.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     */
    public List<PlaceDto> searchFavoritePlacesByName(@NotNull @NotBlank String name) throws Auth0ManagementException {
        User user = auth0UserDataService.getAuthenticatedUserDetails();
        String userId = user.getId();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces = getFavoritePlacesFromMetadata(userMetadata);
        List<PlaceDto> places = favoritePlaces.stream()
                .map(fp -> placesService.getPlaceById(fp.establishmentId()))
                .filter(Objects::nonNull)
                .filter(place -> place.name().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());

        log.debug("Searched favorite places by name '{}' for user {}: found {} results", name, userId, places.size());
        return places;
    }

    /**
     * Counts the number of favorite places for the currently authenticated user.
     *
     * @return the number of favorite places.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     */
    public int countFavoritePlaces() throws Auth0ManagementException {
        User user = auth0UserDataService.getAuthenticatedUserDetails();
        String userId = user.getId();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces = getFavoritePlacesFromMetadata(userMetadata);
        int count = favoritePlaces.size();

        log.debug("Counted favorite places for user {}: {}", userId, count);
        return count;
    }

    /**
     * Rolls back the last favorite place added for the currently authenticated user.
     *
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     * @throws NoFavoritePlacesException if there are no favorite places to rollback.
     */
    public void rollbackLastFavoritePlace() throws Auth0ManagementException {
        User user = auth0UserDataService.getAuthenticatedUserDetails();
        String userId = user.getId();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces = getFavoritePlacesFromMetadata(userMetadata);
        if (favoritePlaces.isEmpty()) {
            log.warn("No favorite places to rollback for user {}", userId);
            throw new NoFavoritePlacesException("No favorite places to rollback for user " + userId);
        }

        FavoritePlaceDto rolledBackPlace = favoritePlaces.removeLast();
        auth0UserDataService.updateAuthenticatedUserMetadata(Map.of(FAVORITE_PLACES_KEY, favoritePlaces));
        log.info("Rolled back the last favorite place for user {}: {}", userId, rolledBackPlace);
    }

    /**
     * Validates that the provided establishment ID is not null or empty and exists.
     *
     * @param establishmentId the establishment ID to validate.
     * @throws InvalidEstablishmentException if the establishment ID is null or empty.
     * @throws InvalidEstablishmentException if the establishment does not exist.
     */
    private void validateEstablishmentId(String establishmentId) {
        if (establishmentId == null || establishmentId.trim().isEmpty()) {
            log.error("Establishment ID must not be null or empty");
            throw new InvalidEstablishmentException("Establishment ID must not be null or empty");
        }
        PlaceDto place = placesService.getPlaceById(establishmentId);
        if (place == null) {
            log.error("Establishment with ID {} does not exist", establishmentId);
            throw new InvalidEstablishmentException("Establishment with ID " + establishmentId + " does not exist");
        }
    }

    /**
     * Extracts favorite places from user metadata.
     *
     * @param userMetadata the user metadata map.
     * @return a list of {@link FavoritePlaceDto} objects.
     */
    @SuppressWarnings("unchecked")
    private List<FavoritePlaceDto> getFavoritePlacesFromMetadata(Map<String, Object> userMetadata) {
        List<FavoritePlaceDto> favoritePlaces = new ArrayList<>();
        if (userMetadata.containsKey(FAVORITE_PLACES_KEY)) {
            List<Map<String, Object>> favoritePlacesMap = (List<Map<String, Object>>) userMetadata.get(FAVORITE_PLACES_KEY);
            favoritePlaces = favoritePlacesMap.stream()
                    .map(map -> objectMapper.convertValue(map, FavoritePlaceDto.class))
                    .collect(Collectors.toList());
        }
        return favoritePlaces;
    }
}