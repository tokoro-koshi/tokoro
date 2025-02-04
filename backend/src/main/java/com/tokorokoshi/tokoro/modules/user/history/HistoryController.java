package com.tokorokoshi.tokoro.modules.user.history;

import com.tokorokoshi.tokoro.modules.exceptions.establishments.InvalidEstablishmentException;
import com.tokorokoshi.tokoro.modules.places.dto.PlaceDto;
import com.tokorokoshi.tokoro.modules.user.history.dto.HistoryEntryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * REST controller for managing user history entries.
 * This controller provides endpoints to add, rollback, retrieve, and clear history entries.
 */
@Tag(name = "History", description = "API for managing user history entries")
@RestController
@RequestMapping("/api/users/history")
public class HistoryController {
    private static final Logger log =
            LoggerFactory.getLogger(HistoryController.class);

    private final HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    /**
     * Adds a history entry to the currently authenticated user's metadata.
     *
     * @param historyEntryDto the history entry to add.
     * @return a response indicating success or failure.
     */
    @Operation(
            summary = "Add a history entry",
            description = "Accepts a request with a JSON body to add a history entry"
    )
    @PostMapping
    public ResponseEntity<String> addHistoryEntry(
            @Parameter(
                    description = "The history entry to add",
                    required = true
            )
            @Valid
            @RequestBody
            HistoryEntryDto historyEntryDto
    ) {
        try {
            historyService.addHistoryEntry(historyEntryDto);
            return ResponseEntity.ok("History entry added successfully");
        } catch (InvalidEstablishmentException e) {
            log.error("Invalid establishment provided", e);
            return ResponseEntity.badRequest().body("Invalid establishment");
        } catch (Exception e) {
            log.error("Error adding history entry", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to add history entry");
        }
    }

    /**
     * Rolls back the last history entry for the currently authenticated user.
     *
     * @return a response indicating success or failure.
     */
    @Operation(
            summary = "Roll back the last history entry",
            description = "Rolls back the last history entry for the currently authenticated user"
    )
    @DeleteMapping("/rollback")
    public ResponseEntity<String> rollbackHistoryEntry() {
        try {
            historyService.rollbackHistoryEntry();
            return ResponseEntity.ok(
                    "Last history entry rolled back successfully");
        } catch (Exception e) {
            log.error("Error rolling back history entry", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to roll back history entry");
        }
    }

    /**
     * Rolls back the last history entry with a specific action for the currently authenticated user.
     *
     * @param action the action to rollback.
     * @return a response indicating success or failure.
     */
    @Operation(
            summary = "Roll back the last history entry by action",
            description = "Rolls back the last history entry with a specific action for the currently authenticated user"
    )
    @DeleteMapping("/rollback/action/{action}")
    public ResponseEntity<String> rollbackHistoryEntryByAction(
            @Parameter(
                    description = "The action to rollback",
                    required = true,
                    example = "CREATE_PLACE"
            )
            @PathVariable
            @NotNull
            @NotBlank
            String action
    ) {
        try {
            historyService.rollbackHistoryEntryByAction(action);
            return ResponseEntity.ok(
                    "Last history entry rolled back successfully");
        } catch (Exception e) {
            log.error("Error rolling back history entry by action", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to roll back history entry by action");
        }
    }

    /**
     * Rolls back all history entries within a specified timestamp range for the currently authenticated user.
     *
     * @param startDate the start timestamp of the range.
     * @param endDate   the end timestamp of the range.
     * @return a response indicating success or failure.
     */
    @Operation(
            summary = "Roll back history entries by timestamp range",
            description = "Rolls back all history entries within a specified timestamp range for the currently authenticated user"
    )
    @DeleteMapping("/rollback/timestamp-range")
    public ResponseEntity<String> rollbackHistoryEntriesByTimestampRange(
            @Parameter(
                    description = "The start timestamp of the range",
                    required = true
            )
            @RequestParam
            @NotNull
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Date startDate,
            @Parameter(
                    description = "The end timestamp of the range",
                    required = true
            )
            @RequestParam
            @NotNull
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Date endDate
    ) {
        try {
            historyService.rollbackHistoryEntriesByTimestampRange(
                    startDate,
                    endDate
            );
            return ResponseEntity.ok(
                    "History entries within the specified timestamp range rolled back successfully");
        } catch (Exception e) {
            log.error(
                    "Error rolling back history entries by timestamp range",
                    e
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to roll back history entries by timestamp range");
        }
    }

    /**
     * Retrieves all history entries for the currently authenticated user.
     *
     * @return a list of {@link HistoryEntryDto} objects.
     */
    @Operation(
            summary = "Get all history entries",
            description = "Returns a list of all history entries for the currently authenticated user"
    )
    @GetMapping
    public ResponseEntity<List<HistoryEntryDto>> getHistoryEntries() {
        try {
            List<HistoryEntryDto> historyEntries =
                    historyService.getHistoryEntries();
            return ResponseEntity.ok(historyEntries);
        } catch (Exception e) {
            log.error("Error retrieving history entries", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .build();
        }
    }

    /**
     * Retrieves all history entries for the currently authenticated user as PlaceDto objects.
     *
     * @return a list of {@link PlaceDto} objects.
     */
    @Operation(
            summary = "Get all history entries as places",
            description = "Returns a list of all history entries for the currently authenticated user as places"
    )
    @GetMapping("/as-places")
    public ResponseEntity<List<PlaceDto>> getHistoryEntriesAsPlaces() {
        try {
            List<PlaceDto> historyEntriesAsPlaces =
                    historyService.getHistoryEntriesAsPlaces();
            return ResponseEntity.ok(historyEntriesAsPlaces);
        } catch (Exception e) {
            log.error("Error retrieving history entries as places", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .build();
        }
    }

    /**
     * Retrieves history entries for a specific action for the currently authenticated user.
     *
     * @param action the action to filter by.
     * @return a list of {@link HistoryEntryDto} objects.
     */
    @Operation(
            summary = "Get history entries by action",
            description = "Returns a list of history entries for a specific action for the currently authenticated user"
    )
    @GetMapping("/action/{action}")
    public ResponseEntity<List<HistoryEntryDto>> getHistoryEntriesByAction(
            @Parameter(
                    description = "The action to filter by",
                    required = true,
                    example = "CREATE_PLACE"
            )
            @PathVariable
            @NotNull
            @NotBlank
            String action
    ) {
        try {
            List<HistoryEntryDto> historyEntries =
                    historyService.getHistoryEntriesByAction(action);
            return ResponseEntity.ok(historyEntries);
        } catch (Exception e) {
            log.error("Error retrieving history entries by action", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .build();
        }
    }

    /**
     * Retrieves history entries for a specific establishment ID for the currently authenticated user.
     *
     * @param establishmentId the establishment ID to filter by.
     * @return a list of {@link HistoryEntryDto} objects.
     */
    @Operation(
            summary = "Get history entries by establishment ID",
            description = "Returns a list of history entries for a specific establishment ID for the currently authenticated user"
    )
    @GetMapping("/establishment-id/{establishmentId}")
    public ResponseEntity<List<HistoryEntryDto>> getHistoryEntriesByEstablishmentId(
            @Parameter(
                    description = "The establishment ID to filter by",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            @NotNull
            @NotBlank
            String establishmentId
    ) {
        try {
            List<HistoryEntryDto> historyEntries =
                    historyService.getHistoryEntriesByEstablishmentId(
                            establishmentId);
            return ResponseEntity.ok(historyEntries);
        } catch (InvalidEstablishmentException e) {
            log.error("Invalid establishment provided", e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error(
                    "Error retrieving history entries by establishment ID",
                    e
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .build();
        }
    }

    /**
     * Retrieves a specific history entry by its timestamp.
     *
     * @param timestamp the timestamp of the history entry to retrieve.
     * @return the {@link HistoryEntryDto} object if found, otherwise a not found response.
     */
    @Operation(
            summary = "Get history entry by timestamp",
            description = "Returns a specific history entry by its timestamp"
    )
    @GetMapping("/timestamp")
    public ResponseEntity<HistoryEntryDto> getHistoryEntryByTimestamp(
            @Parameter(
                    description = "The timestamp of the history entry to retrieve",
                    required = true
            )
            @RequestParam
            @NotNull
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Date timestamp
    ) {
        try {
            HistoryEntryDto historyEntry =
                    historyService.getHistoryEntryByTimestamp(timestamp);
            if (historyEntry != null) {
                return ResponseEntity.ok(historyEntry);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            log.error("Error retrieving history entry by timestamp", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .build();
        }
    }

    /**
     * Clears all history entries for the currently authenticated user.
     *
     * @return a response indicating success or failure.
     */
    @Operation(
            summary = "Clear all history entries",
            description = "Clears all history entries for the currently authenticated user"
    )
    @DeleteMapping
    public ResponseEntity<String> clearHistoryEntries() {
        try {
            historyService.clearHistoryEntries();
            return ResponseEntity.ok("All history entries cleared successfully");
        } catch (Exception e) {
            log.error("Error clearing history entries", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to clear history entries");
        }
    }

    /**
     * Checks if a history entry with the specified timestamp exists for the currently authenticated user.
     *
     * @param timestamp the timestamp to check.
     * @return a response indicating whether the history entry exists.
     */
    @Operation(
            summary = "Check if history entry exists",
            description = "Checks if a history entry with the specified timestamp exists for the currently authenticated user"
    )
    @GetMapping("/exists/timestamp")
    public ResponseEntity<Boolean> isHistoryEntryExists(
            @Parameter(
                    description = "The timestamp to check",
                    required = true
            )
            @RequestParam
            @NotNull
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Date timestamp
    ) {
        try {
            boolean exists = historyService.isHistoryEntryExists(timestamp);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            log.error("Error checking if history entry exists", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .build();
        }
    }

    /**
     * Sorts history entries by the timestamp in ascending order.
     *
     * @return a sorted list of {@link HistoryEntryDto} objects.
     */
    @Operation(
            summary = "Sort history entries by timestamp",
            description = "Sorts history entries by the timestamp in ascending order"
    )
    @GetMapping("/sort/timestamp")
    public ResponseEntity<List<HistoryEntryDto>> getHistoryEntriesSortedByTimestamp() {
        try {
            List<HistoryEntryDto> historyEntries =
                    historyService.getHistoryEntries();
            var comparer = Comparator.comparing(HistoryEntryDto::timestamp);
            return ResponseEntity.ok(historyEntries.stream()
                                                   .sorted(comparer)
                                                   .toList());
        } catch (Exception e) {
            log.error("Error sorting history entries by timestamp", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .build();
        }
    }

    /**
     * Sorts history entries by the timestamp in descending order.
     *
     * @return a sorted list of {@link HistoryEntryDto} objects.
     */
    @Operation(
            summary = "Sort history entries by timestamp in descending order",
            description = "Sorts history entries by the timestamp in descending order"
    )
    @GetMapping("/sort/timestamp-desc")
    public ResponseEntity<List<HistoryEntryDto>> getHistoryEntriesSortedByTimestampDescending() {
        try {
            List<HistoryEntryDto> historyEntries =
                    historyService.getHistoryEntries();
            var comparer = Comparator.comparing(HistoryEntryDto::timestamp)
                                     .reversed();
            return ResponseEntity.ok(historyEntries.stream()
                                                   .sorted(comparer)
                                                   .toList());
        } catch (Exception e) {
            log.error(
                    "Error sorting history entries by timestamp in descending order",
                    e
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .build();
        }
    }

    /**
     * Searches history entries by action.
     *
     * @param action the action to search for.
     * @return a list of {@link HistoryEntryDto} objects that match the search criteria.
     */
    @Operation(
            summary = "Search history entries by action",
            description = "Searches history entries by action"
    )
    @GetMapping("/search/action")
    public ResponseEntity<List<HistoryEntryDto>> searchHistoryEntriesByAction(
            @Parameter(
                    description = "The action to search for",
                    required = true,
                    example = "CREATE_PLACE"
            )
            @RequestParam
            @NotNull
            @NotBlank
            String action
    ) {
        try {
            List<HistoryEntryDto> historyEntries =
                    historyService.getHistoryEntriesByAction(action);
            return ResponseEntity.ok(historyEntries);
        } catch (Exception e) {
            log.error("Error searching history entries by action", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .build();
        }
    }

    /**
     * Searches history entries by establishment ID.
     *
     * @param establishmentId the establishment ID to search for.
     * @return a list of {@link HistoryEntryDto} objects that match the search criteria.
     */
    @Operation(
            summary = "Search history entries by establishment ID",
            description = "Searches history entries by establishment ID"
    )
    @GetMapping("/search/establishment-id")
    public ResponseEntity<List<HistoryEntryDto>> searchHistoryEntriesByEstablishmentId(
            @Parameter(
                    description = "The establishment ID to search for",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b3"
            )
            @RequestParam
            @NotNull
            @NotBlank
            String establishmentId
    ) {
        try {
            List<HistoryEntryDto> historyEntries =
                    historyService.getHistoryEntriesByEstablishmentId(
                            establishmentId);
            return ResponseEntity.ok(historyEntries);
        } catch (InvalidEstablishmentException e) {
            log.error("Invalid establishment provided", e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error searching history entries by establishment ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .build();
        }
    }

    /**
     * Counts the number of history entries for the currently authenticated user.
     *
     * @return the number of history entries.
     */
    @Operation(
            summary = "Count history entries",
            description = "Counts the number of history entries for the currently authenticated user"
    )
    @GetMapping("/count")
    public ResponseEntity<Integer> countHistoryEntries() {
        try {
            int count = historyService.getHistoryEntries().size();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            log.error("Error counting history entries", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .build();
        }
    }
}