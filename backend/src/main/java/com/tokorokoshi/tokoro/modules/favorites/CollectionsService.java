package com.tokorokoshi.tokoro.modules.favorites;

import com.tokorokoshi.tokoro.modules.favorites.dto.CollectionDto;
import com.tokorokoshi.tokoro.modules.favorites.dto.CreateUpdateCollectionDto;
import com.tokorokoshi.tokoro.modules.users.UserService;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.UserUpdateException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CollectionsService {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Autowired
    public CollectionsService(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    private static final String COLLECTIONS_METADATA_KEY = "collections";

    /**
     * Saves a new collection for a user.
     *
     * @param userId                    the Auth0 user ID of the user.
     * @param createUpdateCollectionDto collection data.
     * @return the saved collection.
     */
    public CollectionDto saveCollection(String userId, CreateUpdateCollectionDto createUpdateCollectionDto) {
        List<CollectionDto> collections = getCollectionsForUser(userId);
        UUID collectionId = UUID.randomUUID();
        CollectionDto newCollection = new CollectionDto(
                collectionId,
                createUpdateCollectionDto.name(),
                createUpdateCollectionDto.placesIds(),
                LocalDateTime.now()
        );
        collections.add(newCollection);
        updateCollectionsForUser(userId, collections);
        return newCollection;
    }

    /**
     * Retrieves a collection by ID for a user.
     *
     * @param userId the Auth0 user ID of the user.
     * @param id     the collection ID.
     * @return the collection, or null if not found.
     */
    public CollectionDto getCollectionById(String userId, UUID id) {
        List<CollectionDto> collections = getCollectionsForUser(userId);
        return collections.stream()
                .filter(collection -> collection.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all collections for a user.
     *
     * @param userId the Auth0 user ID of the user.
     * @return a list of collections.
     */
    public List<CollectionDto> getAllCollections(String userId) {
        return getCollectionsForUser(userId);
    }

    /**
     * Updates an existing collection for a user.
     *
     * @param userId                    the Auth0 user ID of the user.
     * @param id                        the collection ID.
     * @param createUpdateCollectionDto collection data to update.
     * @return the updated collection.
     */
    public CollectionDto updateCollection(String userId, UUID id, CreateUpdateCollectionDto createUpdateCollectionDto) {
        List<CollectionDto> collections = getCollectionsForUser(userId);
        Optional<CollectionDto> optionalCollection = collections.stream()
                .filter(collection -> collection.id().equals(id))
                .findFirst();

        if (optionalCollection.isPresent()) {
            CollectionDto existingCollection = optionalCollection.get();
            CollectionDto updatedCollection = existingCollection
                    .withName(createUpdateCollectionDto.name())
                    .withPlacesIds(createUpdateCollectionDto.placesIds())
                    .withCreatedAt(existingCollection.createdAt()); // Keep the original creation time
            collections.set(collections.indexOf(existingCollection), updatedCollection);
            updateCollectionsForUser(userId, collections);
            return updatedCollection;
        } else {
            throw new IllegalArgumentException("Collection not found for id: " + id);
        }
    }

    /**
     * Deletes a collection by ID for a user.
     *
     * @param userId the Auth0 user ID of the user.
     * @param id     the collection ID.
     */
    public void deleteCollection(String userId, UUID id) {
        List<CollectionDto> collections = getCollectionsForUser(userId);
        Optional<CollectionDto> optionalCollection = collections.stream()
                .filter(collection -> collection.id().equals(id))
                .findFirst();

        if (optionalCollection.isPresent()) {
            collections.remove(optionalCollection.get());
            updateCollectionsForUser(userId, collections);
        } else {
            throw new IllegalArgumentException("Collection not found for id: " + id);
        }
    }

    /**
     * Counts the number of collections for a user.
     *
     * @param userId the Auth0 user ID of the user.
     * @return the number of collections.
     */
    public long countCollectionsForUser(String userId) {
        List<CollectionDto> collections = getCollectionsForUser(userId);
        return collections.size();
    }

    /**
     * Adds a favorite place to a collection.
     *
     * @param userId  the Auth0 user ID of the user.
     * @param id      the collection ID.
     * @param placeId the place ID to add.
     * @return the updated collection.
     */
    public CollectionDto addFavoritePlace(String userId, UUID id, String placeId) {
        List<CollectionDto> collections = getCollectionsForUser(userId);
        Optional<CollectionDto> optionalCollection = collections.stream()
                .filter(collection -> collection.id().equals(id))
                .findFirst();

        if (optionalCollection.isPresent()) {
            CollectionDto existingCollection = optionalCollection.get();
            List<String> placesIds = new ArrayList<>(existingCollection.placesIds());
            placesIds.add(placeId);
            CollectionDto updatedCollection = existingCollection
                    .withPlacesIds(placesIds)
                    .withCreatedAt(existingCollection.createdAt()); // Keep the original creation time
            collections.set(collections.indexOf(existingCollection), updatedCollection);
            updateCollectionsForUser(userId, collections);
            return updatedCollection;
        } else {
            throw new IllegalArgumentException("Collection not found for id: " + id);
        }
    }

    /**
     * Removes a favorite place from a collection.
     *
     * @param userId  the Auth0 user ID of the user.
     * @param id      the collection ID.
     * @param placeId the place ID to remove.
     */
    public void removeFavoritePlace(String userId, UUID id, String placeId) {
        List<CollectionDto> collections = getCollectionsForUser(userId);
        Optional<CollectionDto> optionalCollection = collections.stream()
                .filter(collection -> collection.id().equals(id))
                .findFirst();

        if (optionalCollection.isPresent()) {
            CollectionDto existingCollection = optionalCollection.get();
            List<String> placesIds = new ArrayList<>(existingCollection.placesIds());
            placesIds.remove(placeId);
            CollectionDto updatedCollection = existingCollection
                    .withPlacesIds(placesIds)
                    .withCreatedAt(existingCollection.createdAt()); // Keep the original creation time
            collections.set(collections.indexOf(existingCollection), updatedCollection);
            updateCollectionsForUser(userId, collections);
        } else {
            throw new IllegalArgumentException("Collection not found for id: " + id);
        }
    }

    /**
     * Clears all favorite places from a collection.
     *
     * @param userId the Auth0 user ID of the user.
     * @param id     the collection ID.
     */
    public void clearCollection(String userId, UUID id) {
        List<CollectionDto> collections = getCollectionsForUser(userId);
        Optional<CollectionDto> optionalCollection = collections.stream()
                .filter(collection -> collection.id().equals(id))
                .findFirst();

        if (optionalCollection.isPresent()) {
            CollectionDto existingCollection = optionalCollection.get();
            CollectionDto updatedCollection = existingCollection
                    .withPlacesIds(new ArrayList<>())
                    .withCreatedAt(existingCollection.createdAt()); // Keep the original creation time
            collections.set(collections.indexOf(existingCollection), updatedCollection);
            updateCollectionsForUser(userId, collections);
        } else {
            throw new IllegalArgumentException("Collection not found for id: " + id);
        }
    }

    /**
     * Searches collections by name for a user.
     *
     * @param userId the Auth0 user ID of the user.
     * @param name   the name to search for.
     * @return a list of collections matching the name.
     */
    public List<CollectionDto> searchCollectionsByName(String userId, String name) {
        List<CollectionDto> collections = getCollectionsForUser(userId);
        return collections.stream()
                .filter(collection -> collection.name().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    /**
     * Extracts collections from user metadata.
     *
     * @param userMetadata the user metadata map.
     * @return a list of {@link CollectionDto} objects.
     */
    @SuppressWarnings("unchecked")
    private List<CollectionDto> getCollectionsFromMetadata(Map<String, Object> userMetadata) {
        List<CollectionDto> collections = new ArrayList<>();

        if (userMetadata == null) {
            return collections;
        }

        if (userMetadata.containsKey(COLLECTIONS_METADATA_KEY)) {
            List<Map<String, Object>> collectionsMap =
                    (List<Map<String, Object>>) userMetadata.get(
                            COLLECTIONS_METADATA_KEY);
            collections = collectionsMap.stream()
                    .map(map -> objectMapper.convertValue(
                            map,
                            CollectionDto.class
                    ))
                    .collect(Collectors.toList());
        }
        return collections;
    }

    /**
     * Fetches collections for a user from metadata.
     *
     * @param userId the Auth0 user ID of the user.
     * @return a list of collections.
     */
    private List<CollectionDto> getCollectionsForUser(String userId) {
        Map<String, Object> metadata = userService.getUserMetadata(userId);
        return getCollectionsFromMetadata(metadata);
    }

    /**
     * Updates collections for a user in metadata.
     *
     * @param userId      the Auth0 user ID of the user.
     * @param collections the list of collections to update.
     */
    private void updateCollectionsForUser(String userId, List<CollectionDto> collections) {
        try {
            String collectionsJson = objectMapper.writeValueAsString(collections);
            Map<String, Object> metadata = new HashMap<>();
            metadata.put(COLLECTIONS_METADATA_KEY, objectMapper.readTree(collectionsJson));
            userService.updateUserMetadata(userId, metadata);
        } catch (IOException e) {
            throw new UserUpdateException("Error updating collections for user with ID: " + userId, e);
        }
    }
}
