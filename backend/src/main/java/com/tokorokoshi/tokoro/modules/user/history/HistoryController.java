package com.tokorokoshi.tokoro.modules.user.history;

import com.tokorokoshi.tokoro.modules.exceptions.establishments.InvalidEstablishmentException;
import com.tokorokoshi.tokoro.modules.exceptions.history.HistoryEntryNotFoundException;
import com.tokorokoshi.tokoro.modules.exceptions.history.NoHistoryEntriesException;
import com.tokorokoshi.tokoro.modules.places.dto.PlaceDto;
import com.tokorokoshi.tokoro.modules.user.history.dto.HistoryEntryDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing user history entries.
 * This controller provides endpoints to add, rollback, retrieve, and clear history entries.
 */
@RestController
@RequestMapping("/api/v1/users/history")
public class HistoryController {

    private static final Logger log = LoggerFactory.getLogger(HistoryController.class);

    private final HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService) {
        if (historyService == null) {
            throw new IllegalArgumentException("HistoryService must not be null");
        }
        this.historyService = historyService;
    }

    /**
     * Adds a history entry to the currently authenticated user's metadata.
     *
     * @param historyEntryDto the history entry to add.
     * @return a response indicating success or failure.
     */
    @PostMapping
    public ResponseEntity<String> addHistoryEntry(@Valid @RequestBody HistoryEntryDto historyEntryDto) {
        try {
            historyService.addHistoryEntry(historyEntryDto);
            log.info("History entry added successfully for user");
            return ResponseEntity.ok("History entry added successfully");
        } catch (InvalidEstablishmentException e) {
            log.error("Invalid establishment provided", e);
            return ResponseEntity.badRequest().body("Invalid establishment: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error adding history entry", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add history entry: " + e.getMessage());
        }
    }

    /**
     * Rolls back the last history entry for the currently authenticated user.
     *
     * @return a response indicating success or failure.
     */
    @DeleteMapping("/rollback")
    public ResponseEntity<String> rollbackHistoryEntry() {
        try {
            historyService.rollbackHistoryEntry();
            log.info("Last history entry rolled back successfully for user");
            return ResponseEntity.ok("Last history entry rolled back successfully");
        } catch (NoHistoryEntriesException e) {
            log.warn("No history entries to rollback for user");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No history entries to rollback");
        } catch (Exception e) {
            log.error("Error rolling back history entry", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to roll back history entry: " + e.getMessage());
        }
    }

    /**
     * Rolls back the last history entry with a specific action for the currently authenticated user.
     *
     * @param action the action to rollback.
     * @return a response indicating success or failure.
     */
    @DeleteMapping("/rollback/action/{action}")
    public ResponseEntity<String> rollbackHistoryEntryByAction(
            @PathVariable @NotNull @NotBlank String action) {
        try {
            historyService.rollbackHistoryEntryByAction(action);
            log.info("Last history entry with action '{}' rolled back successfully for user", action);
            return ResponseEntity.ok("Last history entry with action '" + action + "' rolled back successfully");
        } catch (NoHistoryEntriesException e) {
            log.warn("No history entries with action '{}' to rollback for user", action);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No history entries with action '" + action + "' to rollback");
        } catch (Exception e) {
            log.error("Error rolling back history entry by action", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to roll back history entry by action: " + e.getMessage());
        }
    }

    /**
     * Rolls back all history entries within a specified timestamp range for the currently authenticated user.
     *
     * @param startDate the start timestamp of the range.
     * @param endDate the end timestamp of the range.
     * @return a response indicating success or failure.
     */
    @DeleteMapping("/rollback/timestamp-range")
    public ResponseEntity<String> rollbackHistoryEntriesByTimestampRange(
            @RequestParam @NotNull Date startDate,
            @RequestParam @NotNull Date endDate) {
        try {
            historyService.rollbackHistoryEntriesByTimestampRange(startDate, endDate);
            log.info("History entries within the specified timestamp range rolled back successfully for user");
            return ResponseEntity.ok("History entries within the specified timestamp range rolled back successfully");
        } catch (NoHistoryEntriesException e) {
            log.warn("No history entries within the specified timestamp range to rollback for user");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No history entries within the specified timestamp range to rollback");
        } catch (Exception e) {
            log.error("Error rolling back history entries by timestamp range", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to roll back history entries by timestamp range: " + e.getMessage());
        }
    }

    /**
     * Retrieves all history entries for the currently authenticated user.
     *
     * @return a list of {@link HistoryEntryDto} objects.
     */
    @GetMapping
    public ResponseEntity<List<HistoryEntryDto>> getHistoryEntries() {
        try {
            List<HistoryEntryDto> historyEntries = historyService.getHistoryEntries();
            log.info("History entries retrieved successfully for user");
            return ResponseEntity.ok(historyEntries);
        } catch (Exception e) {
            log.error("Error retrieving history entries", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of()); // Return an empty list
        }
    }

    /**
     * Retrieves all history entries for the currently authenticated user as PlaceDto objects.
     *
     * @return a list of {@link PlaceDto} objects.
     */
    @GetMapping("/as-places")
    public ResponseEntity<List<PlaceDto>> getHistoryEntriesAsPlaces() {
        try {
            List<PlaceDto> historyEntriesAsPlaces = historyService.getHistoryEntriesAsPlaces();
            log.info("History entries as places retrieved successfully for user");
            return ResponseEntity.ok(historyEntriesAsPlaces);
        } catch (Exception e) {
            log.error("Error retrieving history entries as places", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of()); // Return an empty list
        }
    }

    /**
     * Retrieves history entries for a specific action for the currently authenticated user.
     *
     * @param action the action to filter by.
     * @return a list of {@link HistoryEntryDto} objects.
     */
    @GetMapping("/by-action/{action}")
    public ResponseEntity<List<HistoryEntryDto>> getHistoryEntriesByAction(
            @PathVariable @NotNull @NotBlank String action) {
        try {
            List<HistoryEntryDto> historyEntries = historyService.getHistoryEntriesByAction(action);
            log.info("History entries by action '{}' retrieved successfully for user", action);
            return ResponseEntity.ok(historyEntries);
        } catch (Exception e) {
            log.error("Error retrieving history entries by action", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of()); // Return an empty list
        }
    }

    /**
     * Retrieves history entries for a specific establishment ID for the currently authenticated user.
     *
     * @param establishmentId the establishment ID to filter by.
     * @return a list of {@link HistoryEntryDto} objects.
     */
    @GetMapping("/by-establishment-id/{establishmentId}")
    public ResponseEntity<List<HistoryEntryDto>> getHistoryEntriesByEstablishmentId(
            @PathVariable @NotNull @NotBlank String establishmentId) {
        try {
            List<HistoryEntryDto> historyEntries = historyService.getHistoryEntriesByEstablishmentId(establishmentId);
            log.info("History entries by establishment ID '{}' retrieved successfully for user", establishmentId);
            return ResponseEntity.ok(historyEntries);
        } catch (InvalidEstablishmentException e) {
            log.error("Invalid establishment provided", e);
            return ResponseEntity.badRequest().body(List.of());
        } catch (Exception e) {
            log.error("Error retrieving history entries by establishment ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of()); // Return an empty list
        }
    }

    /**
     * Retrieves a specific history entry by its timestamp.
     *
     * @param timestamp the timestamp of the history entry to retrieve.
     * @return the {@link HistoryEntryDto} object if found, otherwise a not found response.
     */
    @GetMapping("/by-timestamp")
    public ResponseEntity<HistoryEntryDto> getHistoryEntryByTimestamp(
            @RequestParam @NotNull Date timestamp) {
        try {
            HistoryEntryDto historyEntry = historyService.getHistoryEntryByTimestamp(timestamp);
            if (historyEntry != null) {
                log.info("History entry by timestamp '{}' retrieved successfully for user", timestamp);
                return ResponseEntity.ok(historyEntry);
            } else {
                log.warn("History entry with timestamp '{}' not found for user", timestamp);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null); // Return null for not found
            }
        } catch (HistoryEntryNotFoundException e) {
            log.warn("History entry with timestamp '{}' not found for user", timestamp);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Return null for not found
        } catch (Exception e) {
            log.error("Error retrieving history entry by timestamp", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Return null for internal server error
        }
    }

    /**
     * Clears all history entries for the currently authenticated user.
     *
     * @return a response indicating success or failure.
     */
    @DeleteMapping
    public ResponseEntity<String> clearHistoryEntries() {
        try {
            historyService.clearHistoryEntries();
            log.info("All history entries cleared successfully for user");
            return ResponseEntity.ok("All history entries cleared successfully");
        } catch (Exception e) {
            log.error("Error clearing history entries", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to clear history entries: " + e.getMessage());
        }
    }

    /**
     * Checks if a history entry with the specified timestamp exists for the currently authenticated user.
     *
     * @param timestamp the timestamp to check.
     * @return a response indicating whether the history entry exists.
     */
    @GetMapping("/exists/by-timestamp")
    public ResponseEntity<Boolean> isHistoryEntryExists(
            @RequestParam @NotNull Date timestamp) {
        try {
            boolean exists = historyService.isHistoryEntryExists(timestamp);
            log.info("Checked if history entry with timestamp '{}' exists for user: {}", timestamp, exists);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            log.error("Error checking if history entry exists", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(false); // Return false for internal server error
        }
    }

    /**
     * Sorts history entries by the timestamp in ascending order.
     *
     * @return a sorted list of {@link HistoryEntryDto} objects.
     */
    @GetMapping("/sort/timestamp")
    public ResponseEntity<List<HistoryEntryDto>> getHistoryEntriesSortedByTimestamp() {
        try {
            List<HistoryEntryDto> historyEntries = historyService.getHistoryEntries();
            log.info("History entries sorted by timestamp in ascending order for user");
            return ResponseEntity.ok(historyEntries.stream()
                    .sorted(Comparator.comparing(HistoryEntryDto::timestamp))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Error sorting history entries by timestamp", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of()); // Return an empty list
        }
    }

    /**
     * Sorts history entries by the timestamp in descending order.
     *
     * @return a sorted list of {@link HistoryEntryDto} objects.
     */
    @GetMapping("/sort/timestamp-desc")
    public ResponseEntity<List<HistoryEntryDto>> getHistoryEntriesSortedByTimestampDescending() {
        try {
            List<HistoryEntryDto> historyEntries = historyService.getHistoryEntries();
            log.info("History entries sorted by timestamp in descending order for user");
            return ResponseEntity.ok(historyEntries.stream()
                            .sorted(Comparator.comparing(HistoryEntryDto::timestamp).reversed())
                            .collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Error sorting history entries by timestamp in descending order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of()); // Return an empty list
        }
    }

    /**
     * Searches history entries by action.
     *
     * @param action the action to search for.
     * @return a list of {@link HistoryEntryDto} objects that match the search criteria.
     */
    @GetMapping("/search/by-action")
    public ResponseEntity<List<HistoryEntryDto>> searchHistoryEntriesByAction(
            @RequestParam @NotNull @NotBlank String action) {
        try {
            List<HistoryEntryDto> historyEntries = historyService.getHistoryEntriesByAction(action);
            log.info("History entries searched by action '{}' for user", action);
            return ResponseEntity.ok(historyEntries);
        } catch (Exception e) {
            log.error("Error searching history entries by action", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of()); // Return an empty list
        }
    }

    /**
     * Searches history entries by establishment ID.
     *
     * @param establishmentId the establishment ID to search for.
     * @return a list of {@link HistoryEntryDto} objects that match the search criteria.
     */
    @GetMapping("/search/by-establishment-id")
    public ResponseEntity<List<HistoryEntryDto>> searchHistoryEntriesByEstablishmentId(
            @RequestParam @NotNull @NotBlank String establishmentId) {
        try {
            List<HistoryEntryDto> historyEntries = historyService.getHistoryEntriesByEstablishmentId(establishmentId);
            log.info("History entries searched by establishment ID '{}' for user", establishmentId);
            return ResponseEntity.ok(historyEntries);
        } catch (InvalidEstablishmentException e) {
            log.error("Invalid establishment provided", e);
            return ResponseEntity.badRequest().body(List.of());
        } catch (Exception e) {
            log.error("Error searching history entries by establishment ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of()); // Return an empty list
        }
    }

    /**
     * Counts the number of history entries for the currently authenticated user.
     *
     * @return the number of history entries.
     */
    @GetMapping("/count")
    public ResponseEntity<Integer> countHistoryEntries() {
        try {
            int count = historyService.getHistoryEntries().size();
            log.info("History entries count retrieved successfully for user: {}", count);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            log.error("Error counting history entries", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(0); // Return 0 for internal server error
        }
    }
}