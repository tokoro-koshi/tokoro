package com.tokorokoshi.tokoro.modules.user.history;

import com.auth0.json.mgmt.users.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tokorokoshi.tokoro.modules.exceptions.auth0.Auth0ManagementException;
import com.tokorokoshi.tokoro.modules.auth0.Auth0UserDataService;
import com.tokorokoshi.tokoro.modules.exceptions.establishments.InvalidEstablishmentException;
import com.tokorokoshi.tokoro.modules.exceptions.history.HistoryEntryNotFoundException;
import com.tokorokoshi.tokoro.modules.exceptions.history.NoHistoryEntriesException;
import com.tokorokoshi.tokoro.modules.places.PlacesService;
import com.tokorokoshi.tokoro.modules.places.dto.PlaceDto;
import com.tokorokoshi.tokoro.modules.user.history.dto.HistoryEntryDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class responsible for managing user history entries.
 * This service interacts with user metadata stored in Auth0 and provides methods to add, rollback, and retrieve history entries.
 */
@Service
public class HistoryService {

    private static final Logger log = LoggerFactory.getLogger(HistoryService.class);

    private static final String HISTORY_KEY = "history";

    @Value("${max_history_entries}")
    private int MAX_HISTORY_ENTRIES;

    private final Auth0UserDataService auth0UserDataService;
    private final PlacesService placesService;
    private final ObjectMapper objectMapper;

    @Autowired
    public HistoryService(Auth0UserDataService auth0UserDataService, PlacesService placesService, ObjectMapper objectMapper) {
        this.auth0UserDataService = auth0UserDataService;
        this.placesService = placesService;
        this.objectMapper = objectMapper;
    }

    /**
     * Adds a history entry to the currently authenticated user's metadata.
     *
     * @param historyEntryDto the history entry to add.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     * @throws InvalidEstablishmentException if the establishment ID is invalid.
     */
    public void addHistoryEntry(@Valid HistoryEntryDto historyEntryDto) throws Auth0ManagementException {
        validateEstablishmentId(historyEntryDto.establishmentId());

        User user = auth0UserDataService.getAuthenticatedUserDetails();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<HistoryEntryDto> historyEntries = getHistoryEntriesFromMetadata(userMetadata);
        if (historyEntries.size() >= MAX_HISTORY_ENTRIES) {
            log.warn("Maximum history entries reached for user {}. Removing the oldest entry.", user.getId());
            historyEntries.removeFirst(); // Remove the oldest entry if the limit is reached
        }

        historyEntries.add(historyEntryDto);
        auth0UserDataService.updateAuthenticatedUserMetadata(Map.of(HISTORY_KEY, historyEntries));
        log.info("Added history entry for action '{}' at establishment ID {} for user {}", historyEntryDto.action(), historyEntryDto.establishmentId(), user.getId());
    }

    /**
     * Rolls back the last history entry for the currently authenticated user.
     *
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     * @throws NoHistoryEntriesException if there are no history entries to rollback.
     */
    public void rollbackHistoryEntry() throws Auth0ManagementException {
        User user = auth0UserDataService.getAuthenticatedUserDetails();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<HistoryEntryDto> historyEntries = getHistoryEntriesFromMetadata(userMetadata);
        if (historyEntries.isEmpty()) {
            log.warn("No history entries to rollback for user {}", user.getId());
            throw new NoHistoryEntriesException("No history entries to rollback for user " + user.getId());
        }

        HistoryEntryDto rolledBackEntry = historyEntries.removeLast();
        auth0UserDataService.updateAuthenticatedUserMetadata(Map.of(HISTORY_KEY, historyEntries));
        log.info("Rolled back the last history entry for user {}: {}", user.getId(), rolledBackEntry);
    }

    /**
     * Rolls back the last history entry with a specific action for the currently authenticated user.
     *
     * @param action the action to rollback.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     * @throws NoHistoryEntriesException if there are no history entries with the specified action to rollback.
     */
    public void rollbackHistoryEntryByAction(@NotNull String action) throws Auth0ManagementException {
        User user = auth0UserDataService.getAuthenticatedUserDetails();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<HistoryEntryDto> historyEntries = getHistoryEntriesFromMetadata(userMetadata);
        if (historyEntries.isEmpty()) {
            log.warn("No history entries to rollback for user {}", user.getId());
            throw new NoHistoryEntriesException("No history entries to rollback for user " + user.getId());
        }

        List<HistoryEntryDto> filteredEntries = historyEntries.stream()
                .filter(he -> he.action().equals(action))
                .toList();

        if (filteredEntries.isEmpty()) {
            log.warn("No history entries with action '{}' to rollback for user {}", action, user.getId());
            throw new NoHistoryEntriesException("No history entries with action '" + action + "' to rollback for user " + user.getId());
        }

        HistoryEntryDto rolledBackEntry = filteredEntries.getLast();
        historyEntries.remove(rolledBackEntry);
        auth0UserDataService.updateAuthenticatedUserMetadata(Map.of(HISTORY_KEY, historyEntries));
        log.info("Rolled back the last history entry with action '{}' for user {}: {}", action, user.getId(), rolledBackEntry);
    }

    /**
     * Rolls back all history entries within a specified timestamp range for the currently authenticated user.
     *
     * @param startDate the start timestamp of the range.
     * @param endDate the end timestamp of the range.
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     * @throws NoHistoryEntriesException if there are no history entries within the specified timestamp range to rollback.
     */
    public void rollbackHistoryEntriesByTimestampRange(@NotNull Date startDate, @NotNull Date endDate) throws Auth0ManagementException {
        User user = auth0UserDataService.getAuthenticatedUserDetails();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<HistoryEntryDto> historyEntries = getHistoryEntriesFromMetadata(userMetadata);
        if (historyEntries.isEmpty()) {
            log.warn("No history entries to rollback for user {}", user.getId());
            throw new NoHistoryEntriesException("No history entries to rollback for user " + user.getId());
        }

        List<HistoryEntryDto> entriesToRemove = historyEntries.stream()
                .filter(he -> !he.timestamp().before(startDate) && !he.timestamp().after(endDate))
                .toList();

        if (entriesToRemove.isEmpty()) {
            log.warn("No history entries within the specified timestamp range to rollback for user {}", user.getId());
            throw new NoHistoryEntriesException("No history entries within the specified timestamp range to rollback for user " + user.getId());
        }

        historyEntries.removeAll(entriesToRemove);
        auth0UserDataService.updateAuthenticatedUserMetadata(Map.of(HISTORY_KEY, historyEntries));
        log.info("Rolled back {} history entries within the specified timestamp range for user {}", entriesToRemove.size(), user.getId());
    }

    /**
     * Retrieves all history entries for the currently authenticated user.
     *
     * @return a list of {@link HistoryEntryDto} objects.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     */
    public List<HistoryEntryDto> getHistoryEntries() throws Auth0ManagementException {
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        return getHistoryEntriesFromMetadata(userMetadata);
    }

    /**
     * Retrieves all history entries for the currently authenticated user as PlaceDto objects.
     *
     * @return a list of {@link PlaceDto} objects.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     */
    public List<PlaceDto> getHistoryEntriesAsPlaces() throws Auth0ManagementException {
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<HistoryEntryDto> historyEntries = getHistoryEntriesFromMetadata(userMetadata);
        return historyEntries.stream()
                .map(he -> placesService.getPlaceById(he.establishmentId()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves history entries for a specific action for the currently authenticated user.
     *
     * @param action the action to filter by.
     * @return a list of {@link HistoryEntryDto} objects.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     */
    public List<HistoryEntryDto> getHistoryEntriesByAction(@NotNull String action) throws Auth0ManagementException {
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<HistoryEntryDto> historyEntries = getHistoryEntriesFromMetadata(userMetadata);
        return historyEntries.stream()
                .filter(he -> he.action().equals(action))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves history entries for a specific establishment ID for the currently authenticated user.
     *
     * @param establishmentId the establishment ID to filter by.
     * @return a list of {@link HistoryEntryDto} objects.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     * @throws InvalidEstablishmentException if the establishment ID is invalid.
     */
    public List<HistoryEntryDto> getHistoryEntriesByEstablishmentId(@NotNull String establishmentId) throws Auth0ManagementException {
        validateEstablishmentId(establishmentId);

        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<HistoryEntryDto> historyEntries = getHistoryEntriesFromMetadata(userMetadata);
        return historyEntries.stream()
                .filter(he -> he.establishmentId().equals(establishmentId))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific history entry by its timestamp.
     *
     * @param timestamp the timestamp of the history entry to retrieve.
     * @return the {@link HistoryEntryDto} object if found, otherwise null.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     * @throws HistoryEntryNotFoundException if the history entry with the specified timestamp does not exist.
     */
    public HistoryEntryDto getHistoryEntryByTimestamp(@NotNull Date timestamp) throws Auth0ManagementException {
        User user = auth0UserDataService.getAuthenticatedUserDetails();
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<HistoryEntryDto> historyEntries = getHistoryEntriesFromMetadata(userMetadata);
        Optional<HistoryEntryDto> historyEntry = historyEntries.stream()
                .filter(he -> he.timestamp().equals(timestamp))
                .findFirst();

        if (historyEntry.isPresent()) {
            return historyEntry.get();
        }

        log.warn("History entry with timestamp {} does not exist for user {}", timestamp, user.getId());
        throw new HistoryEntryNotFoundException("History entry with timestamp " + timestamp + " does not exist for user " + user.getId());
    }

    /**
     * Clears all history entries for the currently authenticated user.
     *
     * @throws Auth0ManagementException if there is an error updating the user metadata.
     */
    public void clearHistoryEntries() throws Auth0ManagementException {
        User user = auth0UserDataService.getAuthenticatedUserDetails();
        auth0UserDataService.updateAuthenticatedUserMetadata(Map.of(HISTORY_KEY, new ArrayList<>()));
        log.info("Cleared all history entries for user {}", user.getId());
    }

    /**
     * Checks if a history entry with the specified timestamp exists for the currently authenticated user.
     *
     * @param timestamp the timestamp to check.
     * @return true if the history entry exists, false otherwise.
     * @throws Auth0ManagementException if there is an error fetching the user metadata.
     */
    public boolean isHistoryEntryExists(@NotNull Date timestamp) throws Auth0ManagementException {
        Map<String, Object> userMetadata = auth0UserDataService.getAuthenticatedUserMetadata();

        List<HistoryEntryDto> historyEntries = getHistoryEntriesFromMetadata(userMetadata);
        return historyEntries.stream().anyMatch(he -> he.timestamp().equals(timestamp));
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
            throw new InvalidEstablishmentException("Establishment ID must not be null or empty");
        }
        PlaceDto place = placesService.getPlaceById(establishmentId);
        if (place == null) {
            throw new InvalidEstablishmentException("Establishment with ID " + establishmentId + " does not exist");
        }
    }

    /**
     * Extracts history entries from user metadata.
     *
     * @param userMetadata the user metadata map.
     * @return a list of {@link HistoryEntryDto} objects.
     */
    @SuppressWarnings("unchecked")
    private List<HistoryEntryDto> getHistoryEntriesFromMetadata(Map<String, Object> userMetadata) {
        List<HistoryEntryDto> historyEntries = new ArrayList<>();
        if (userMetadata.containsKey(HISTORY_KEY)) {
            List<Map<String, Object>> historyEntriesMap = (List<Map<String, Object>>) userMetadata.get(HISTORY_KEY);
            historyEntries = historyEntriesMap.stream()
                    .map(map -> objectMapper.convertValue(map, HistoryEntryDto.class))
                    .collect(Collectors.toList());
        }
        return historyEntries;
    }
}