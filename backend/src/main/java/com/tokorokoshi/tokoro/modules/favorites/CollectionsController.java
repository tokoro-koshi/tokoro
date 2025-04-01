package com.tokorokoshi.tokoro.modules.favorites;

import com.tokorokoshi.tokoro.dto.PaginationDto;
import com.tokorokoshi.tokoro.modules.error.NotFoundException;
import com.tokorokoshi.tokoro.modules.favorites.dto.AddFavoriteDto;
import com.tokorokoshi.tokoro.modules.favorites.dto.CollectionDto;
import com.tokorokoshi.tokoro.modules.favorites.dto.CreateUpdateCollectionDto;
import com.tokorokoshi.tokoro.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "User Collections", description = "API for managing user collections")
@RestController
@RequestMapping("/collections")
public class CollectionsController {

    private final CollectionsService collectionService;
    private final PagedResourcesAssembler<CollectionDto> pagedResourcesAssembler;
    private final Logger logger;

    public CollectionsController(
            CollectionsService collectionService,
            PagedResourcesAssembler<CollectionDto> pagedResourcesAssembler
    ) {
        this.collectionService = collectionService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.logger = Logger.getLogger(CollectionsController.class.getName());
    }

    @Operation(
            summary = "Save a new collection",
            description = "Accepts a request with JSON data to save a new collection, and returns the saved collection"
    )
    @PostMapping(
            value = {"", "/"},
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CollectionDto> saveCollection(
            @Parameter(description = "The collection to save", required = true)
            @RequestBody
            CreateUpdateCollectionDto createUpdateCollectionDto
    ) {
        String userId = SecurityUtils.getAuthenticatedUserId();
        return ResponseEntity.ok(this.collectionService.saveCollection(userId, createUpdateCollectionDto));
    }

    @Operation(
            summary = "Get all collections for a user",
            description = "Returns a paginated list of all collections for a user"
    )
    @GetMapping(value = {"/users/{userId}"}, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PaginationDto<CollectionDto>> getAllCollections(
            @Parameter(description = "The user ID", required = true)
            @PathVariable
            String userId,

            @Parameter(description = "The page number to get", example = "0")
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
        List<CollectionDto> collections = this.collectionService.getAllCollections(userId);
        long total = this.collectionService.countCollectionsForUser(userId);

        List<CollectionDto> pagedCollections = collections.stream()
                .skip((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        Page<CollectionDto> pageResult = new PageImpl<>(pagedCollections, pageable, total);
        var pagination = PaginationDto.fromEntityModel(
                this.pagedResourcesAssembler.toModel(pageResult)
        );
        return ResponseEntity.ok(pagination);
    }

    @Operation(
            summary = "Get all collections for the current user",
            description = "Returns a paginated list of all collections for the current user"
    )
    @GetMapping(value = {"/me"}, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PaginationDto<CollectionDto>> getAllCollections(
            @Parameter(description = "The page number to get", example = "0")
            @RequestParam(defaultValue = "0")
            int page,

            @Parameter(
                    description = "The number of items per page",
                    example = "20"
            )
            @RequestParam(defaultValue = "20")
            int size
    ) {
        String userId = SecurityUtils.getAuthenticatedUserId();
        return this.getAllCollections(userId, page, size);
    }

    @Operation(
            summary = "Get a collection by ID",
            description = "Returns the collection with the given ID for a user"
    )
    @GetMapping(value = "/{collectionId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionDto> getCollectionById(
            @Parameter(
                    description = "The ID of the collection to get",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            UUID collectionId
    ) {
        String userId = SecurityUtils.getAuthenticatedUserId();
        var collection = this.collectionService.getCollectionById(userId, collectionId);
        if (collection == null) {
            throw new NotFoundException("Collection not found");
        }
        return ResponseEntity.ok(collection);
    }

    @Operation(
            summary = "Update a collection",
            description = "Accepts a request with JSON data to update a collection, and returns the updated collection"
    )
    @PutMapping(
            value = "/{collectionId}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CollectionDto> updateCollection(
            @Parameter(
                    description = "The ID of the collection to update",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            UUID collectionId,

            @Parameter(description = "The collection to update", required = true)
            @RequestBody
            CreateUpdateCollectionDto createUpdateCollectionDto
    ) {
        String userId = SecurityUtils.getAuthenticatedUserId();
        CollectionDto updatedCollection = this.collectionService.updateCollection(userId, collectionId, createUpdateCollectionDto);
        if (updatedCollection == null) {
            throw new NotFoundException("Collection not found");
        }
        return ResponseEntity.ok(updatedCollection);
    }

    @Operation(
            summary = "Delete a collection",
            description = "Deletes the collection with the given ID for a user"
    )
    @DeleteMapping(value = "/{collectionId}")
    public ResponseEntity<?> deleteCollection(
            @Parameter(
                    description = "The ID of the collection to delete",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            UUID collectionId
    ) {
        try {
            String userId = SecurityUtils.getAuthenticatedUserId();
            this.collectionService.deleteCollection(userId, collectionId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Collection not found");
        } catch (Exception e) {
            this.logger.severe(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Add a favorite place to a collection",
            description = "Adds a favorite place to the specified collection for a user"
    )
    @PostMapping(
            value = "/{collectionId}/places",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CollectionDto> addFavoritePlace(
            @Parameter(
                    description = "The ID of the collection",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            UUID collectionId,

            @Parameter(description = "The place ID to add", required = true)
            @RequestBody()
            AddFavoriteDto addFavoriteDto
    ) {
        String userId = SecurityUtils.getAuthenticatedUserId();
        return ResponseEntity.ok(this.collectionService.addFavoritePlace(userId, collectionId, addFavoriteDto.placeId()));
    }

    @Operation(
            summary = "Remove a favorite place from a collection",
            description = "Removes a favorite place from the specified collection for a user"
    )
    @DeleteMapping(value = "/{collectionId}/places/{placeId}")
    public ResponseEntity<CollectionDto> removeFavoritePlace(
            @Parameter(
                    description = "The ID of the collection",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            UUID collectionId,

            @Parameter(
                    description = "The place ID to remove",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String placeId
    ) {
        try {
            String userId = SecurityUtils.getAuthenticatedUserId();
            this.collectionService.removeFavoritePlace(userId, collectionId, placeId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Collection not found");
        } catch (Exception e) {
            this.logger.severe(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Clear all favorite places from a collection",
            description = "Clears all favorite places from the specified collection for a user"
    )
    @DeleteMapping(value = "/{collectionId}/places")
    public ResponseEntity<CollectionDto> clearCollection(
            @Parameter(
                    description = "The ID of the collection",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            UUID collectionId
    ) {
        try {
            String userId = SecurityUtils.getAuthenticatedUserId();
            this.collectionService.clearCollection(userId, collectionId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Collection not found");
        } catch (Exception e) {
            this.logger.severe(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Search collections by name",
            description = "Returns a list of collections matching the specified name for a user"
    )
    @GetMapping(value = "/search", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CollectionDto>> searchCollectionsByName(
            @Parameter(description = "The name to search for", required = true)
            @RequestParam
            String name
    ) {
        String userId = SecurityUtils.getAuthenticatedUserId();
        List<CollectionDto> collections = this.collectionService.searchCollectionsByName(userId, name);
        return ResponseEntity.ok(collections);
    }
}
