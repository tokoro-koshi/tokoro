package com.tokorokoshi.tokoro.modules.user.favorites.places;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tokorokoshi.tokoro.modules.auth0.Auth0UserDataService;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.Auth0ManagementException;
import com.tokorokoshi.tokoro.modules.exceptions.establishments.InvalidEstablishmentException;
import com.tokorokoshi.tokoro.modules.places.PlacesService;
import com.tokorokoshi.tokoro.modules.places.dto.PlaceDto;
import com.tokorokoshi.tokoro.modules.user.favorites.places.dto.FavoritePlaceDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class responsible for managing favorite places for users.
 * This class handles operations such as adding, updating, removing, and retrieving favorite places.
 */
@Service
public class FavoritePlacesService {

    private static final Logger log =
            LoggerFactory.getLogger(FavoritePlacesService.class);

    private static final String FAVORITE_PLACES_KEY = "favorite_places";

    private final Auth0UserDataService auth0UserDataService;
    private final PlacesService placesService;
    private final ObjectMapper objectMapper;

    @Autowired
    public FavoritePlacesService(
            Auth0UserDataService auth0UserDataService,
            PlacesService placesService,
            ObjectMapper objectMapper
    ) {
        this.auth0UserDataService = auth0UserDataService;
        this.placesService = placesService;
        this.objectMapper = objectMapper;
    }

    /**
     * Adds a favorite place to the currently authenticated user's metadata.
     *
     * @param favoritePlaceDto the favorite place to add.
     * @throws Auth0ManagementException      if there is an error updating the user metadata.
     * @throws InvalidEstablishmentException if the establishment ID is invalid.
     */
    public void addFavoritePlace(@Valid FavoritePlaceDto favoritePlaceDto) {
        validateEstablishmentId(favoritePlaceDto.establishmentId());

        if (favoritePlaceDto.addedAt() == null) {
            favoritePlaceDto = new FavoritePlaceDto(
                    favoritePlaceDto.establishmentId(),
                    new Date(),
                    favoritePlaceDto.notes(),
                    favoritePlaceDto.rating()
            );
        }

        Map<String, Object> userMetadata =
                auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces =
                getFavoritePlacesFromMetadata(userMetadata);
        @Valid FavoritePlaceDto finalFavoritePlaceDto = favoritePlaceDto;
        if (favoritePlaces.stream().anyMatch(fp -> fp.establishmentId().equals(
                finalFavoritePlaceDto.establishmentId()))) {
            return;
        }

        favoritePlaces.add(favoritePlaceDto);
        auth0UserDataService.updateAuthenticatedUserMetadata(Map.of(
                FAVORITE_PLACES_KEY,
                favoritePlaces
        ));
    }

    /**
     * Updates a favorite place in the currently authenticated user's metadata.
     *
     * @param establishmentId  the establishment ID of the favorite place to update.
     * @param favoritePlaceDto the updated favorite place.
     * @throws Auth0ManagementException      if there is an error updating the user metadata.
     * @throws InvalidEstablishmentException if the establishment ID is invalid.
     */
    public void updateFavoritePlace(
            @NotNull String establishmentId,
            @Valid FavoritePlaceDto favoritePlaceDto
    ) {
        validateEstablishmentId(favoritePlaceDto.establishmentId());

        Map<String, Object> userMetadata =
                auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces =
                getFavoritePlacesFromMetadata(userMetadata);
        if (favoritePlaces.stream()
                          .noneMatch(fp -> fp.establishmentId()
                                             .equals(establishmentId))) {
            return;
        }

        favoritePlaces = favoritePlaces.stream()
                                       .map(fp -> fp.establishmentId()
                                                    .equals(establishmentId)
                                                  ? favoritePlaceDto
                                                  : fp)
                                       .toList();

        auth0UserDataService.updateAuthenticatedUserMetadata(Map.of(
                FAVORITE_PLACES_KEY,
                favoritePlaces
        ));
    }

    /**
     * Removes a favorite place from the currently authenticated user's metadata.
     *
     * @param establishmentId the establishment ID of the favorite place to remove.
     * @throws Auth0ManagementException      if there is an error updating the user metadata.
     * @throws InvalidEstablishmentException if the establishment ID is invalid.
     */
    public void removeFavoritePlace(@NotNull String establishmentId) {
        validateEstablishmentId(establishmentId);

        Map<String, Object> userMetadata =
                auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces =
                getFavoritePlacesFromMetadata(userMetadata);
        if (favoritePlaces.stream()
                          .noneMatch(fp -> fp.establishmentId()
                                             .equals(establishmentId))) {
            return;
        }

        favoritePlaces = favoritePlaces.stream()
                                       .filter(fp -> !fp.establishmentId()
                                                        .equals(establishmentId))
                                       .toList();

        auth0UserDataService.updateAuthenticatedUserMetadata(Map.of(
                FAVORITE_PLACES_KEY,
                favoritePlaces
        ));
    }

    /**
     * Retrieves all favorite places for the currently authenticated user as PlaceDto objects.
     *
     * @return a list of {@link PlaceDto} objects.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     */
    public List<PlaceDto> getFavoritePlaces() {
        Map<String, Object> userMetadata =
                auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces =
                getFavoritePlacesFromMetadata(userMetadata);

        return favoritePlaces.stream()
                             .map(fp -> placesService.getPlaceById(fp.establishmentId()))
                             .filter(Objects::nonNull)
                             .toList();
    }

    /**
     * Retrieves a specific favorite place by its establishment ID as a PlaceDto object.
     *
     * @param establishmentId the establishment ID of the favorite place to retrieve.
     * @return the {@link PlaceDto} object if found, otherwise null.
     * @throws Auth0ManagementException      if there is an error fetching the user metadata.
     * @throws InvalidEstablishmentException if the establishment ID is invalid.
     */
    public PlaceDto getFavoritePlaceById(@NotNull String establishmentId) {
        validateEstablishmentId(establishmentId);

        Map<String, Object> userMetadata =
                auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces =
                getFavoritePlacesFromMetadata(userMetadata);
        Optional<FavoritePlaceDto> favoritePlace = favoritePlaces.stream()
                                                                 .filter(fp -> fp.establishmentId()
                                                                                 .equals(establishmentId))
                                                                 .findFirst();

        return favoritePlace.map(favoritePlaceDto -> placesService.getPlaceById(
                favoritePlaceDto.establishmentId())).orElse(null);
    }

    /**
     * Clears all favorite places for the currently authenticated user.
     *
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     */
    public void clearFavoritePlaces() {
        auth0UserDataService.updateAuthenticatedUserMetadata(Map.of(
                FAVORITE_PLACES_KEY,
                new ArrayList<>()
        ));
    }

    /**
     * Checks if a place is already a favorite for the currently authenticated user.
     *
     * @param establishmentId the establishment ID to check.
     * @return true if the place is a favorite, false otherwise.
     * @throws Auth0ManagementException      if there is an error fetching the user metadata.
     * @throws InvalidEstablishmentException if the establishment ID is invalid.
     */
    public boolean isFavoritePlace(@NotNull String establishmentId) {
        validateEstablishmentId(establishmentId);

        Map<String, Object> userMetadata =
                auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces =
                getFavoritePlacesFromMetadata(userMetadata);

        return favoritePlaces.stream()
                             .anyMatch(fp -> fp.establishmentId()
                                               .equals(establishmentId));
    }

    /**
     * Sorts favorite places by the added date in ascending order.
     *
     * @return a sorted list of {@link PlaceDto} objects.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     */
    public List<PlaceDto> getFavoritePlacesSortedByDate() {
        Map<String, Object> userMetadata =
                auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces =
                getFavoritePlacesFromMetadata(userMetadata);

        return favoritePlaces.stream()
                             .sorted(Comparator.comparing(FavoritePlaceDto::addedAt))
                             .map(fp -> placesService.getPlaceById(fp.establishmentId()))
                             .filter(Objects::nonNull)
                             .toList();
    }

    /**
     * Sorts favorite places by the added date in descending order.
     *
     * @return a sorted list of {@link PlaceDto} objects.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     */
    public List<PlaceDto> getFavoritePlacesSortedByDateDescending() {
        Map<String, Object> userMetadata =
                auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces =
                getFavoritePlacesFromMetadata(userMetadata);

        return favoritePlaces.stream()
                             .sorted(Comparator.comparing(FavoritePlaceDto::addedAt)
                                               .reversed())
                             .map(fp -> placesService.getPlaceById(fp.establishmentId()))
                             .filter(Objects::nonNull)
                             .toList();
    }

    /**
     * Sorts favorite places by the rating in descending order.
     *
     * @return a sorted list of {@link PlaceDto} objects.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     */
    public List<PlaceDto> getFavoritePlacesSortedByRating() {
        Map<String, Object> userMetadata =
                auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces =
                getFavoritePlacesFromMetadata(userMetadata);

        return favoritePlaces.stream()
                             .filter(fp -> fp.rating() != null)
                             .sorted(Comparator.comparing(FavoritePlaceDto::rating)
                                               .reversed())
                             .map(fp -> placesService.getPlaceById(fp.establishmentId()))
                             .filter(Objects::nonNull)
                             .toList();
    }

    /**
     * Searches favorite places by name.
     *
     * @param name the name to search for.
     * @return a list of {@link PlaceDto} objects that match the search criteria.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     */
    public List<PlaceDto> searchFavoritePlacesByName(
            @NotNull @NotBlank String name
    ) {
        Map<String, Object> userMetadata =
                auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces =
                getFavoritePlacesFromMetadata(userMetadata);

        return favoritePlaces.stream()
                             .map(fp -> placesService.getPlaceById(fp.establishmentId()))
                             .filter(Objects::nonNull)
                             .filter(place -> place.name()
                                                   .toLowerCase()
                                                   .contains(name.toLowerCase()))
                             .toList();
    }

    /**
     * Counts the number of favorite places for the currently authenticated user.
     *
     * @return the number of favorite places.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     */
    public int countFavoritePlaces() {
        Map<String, Object> userMetadata =
                auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces =
                getFavoritePlacesFromMetadata(userMetadata);

        return favoritePlaces.size();
    }

    /**
     * Rolls back the last favorite place added for the currently authenticated user.
     *
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     */
    public void rollbackLastFavoritePlace() {
        Map<String, Object> userMetadata =
                auth0UserDataService.getAuthenticatedUserMetadata();

        List<FavoritePlaceDto> favoritePlaces =
                getFavoritePlacesFromMetadata(userMetadata);
        if (favoritePlaces.isEmpty()) {
            return;
        }

        favoritePlaces.removeLast();
        auth0UserDataService.updateAuthenticatedUserMetadata(Map.of(
                FAVORITE_PLACES_KEY,
                favoritePlaces
        ));
    }

    /**
     * Validates that the provided establishment ID is not null or empty and exists.
     *
     * @param establishmentId the establishment ID to validate.
     * @throws InvalidEstablishmentException if the establishment does not exist.
     */
    private void validateEstablishmentId(String establishmentId) {
        PlaceDto place = placesService.getPlaceById(establishmentId);
        if (place == null) {
            log.error(
                    "Establishment with ID {} does not exist",
                    establishmentId
            );
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
            List<Map<String, Object>> favoritePlacesMap =
                    (List<Map<String, Object>>) userMetadata.get(
                            FAVORITE_PLACES_KEY);
            favoritePlaces = favoritePlacesMap.stream()
                                              .map(map -> objectMapper.convertValue(
                                                      map,
                                                      FavoritePlaceDto.class
                                              ))
                                              .toList();
        }
        return favoritePlaces;
    }
}