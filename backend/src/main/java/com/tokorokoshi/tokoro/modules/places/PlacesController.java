package com.tokorokoshi.tokoro.modules.places;

import com.tokorokoshi.tokoro.database.Message;
import com.tokorokoshi.tokoro.dto.PaginationDto;
import com.tokorokoshi.tokoro.dto.Response;
import com.tokorokoshi.tokoro.modules.chats.ChatHistoryService;
import com.tokorokoshi.tokoro.modules.chats.dto.ChatHistoryDto;
import com.tokorokoshi.tokoro.modules.error.NotFoundException;
import com.tokorokoshi.tokoro.modules.places.dto.CoordinateDto;
import com.tokorokoshi.tokoro.modules.places.dto.CreateUpdatePlaceDto;
import com.tokorokoshi.tokoro.modules.places.dto.PlaceDto;
import com.tokorokoshi.tokoro.modules.places.dto.SearchDto;
import com.tokorokoshi.tokoro.modules.tags.TagsService;
import com.tokorokoshi.tokoro.modules.tags.dto.TagDto;
import com.tokorokoshi.tokoro.modules.tags.dto.TagsDto;
import com.tokorokoshi.tokoro.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Tag(name = "Places", description = "API for managing places")
@RestController
@RequestMapping("/places")
public class PlacesController {
    private final PlacesService placesService;
    private final TagsService tagsService;
    private final ChatHistoryService chatHistoryService;
    private final Logger logger;
    private final PagedResourcesAssembler<PlaceDto> pagedResourcesAssembler;

    public PlacesController(
            PlacesService placesService,
            PagedResourcesAssembler<PlaceDto> pagedResourcesAssembler,
            TagsService tagsService,
            ChatHistoryService chatHistoryService
    ) {
        this.placesService = placesService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.logger = Logger.getLogger(PlacesController.class.getName());
        this.tagsService = tagsService;
        this.chatHistoryService = chatHistoryService;
    }

    @Operation(
            summary = "Save a new place",
            description = "Accepts a request with a form data to save a new place, and returns the saved place"
    )
    @PostMapping(
            value = {"", "/"},
            consumes = MULTIPART_FORM_DATA_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PlaceDto> savePlace(
            @Parameter(description = "The place to save", required = true)
            @ModelAttribute
            CreateUpdatePlaceDto place
    ) {
        return ResponseEntity.ok(this.placesService.savePlace(place));
    }

    @Operation(
            summary = "Get a place by ID",
            description = "Returns the place with the given ID"
    )
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PlaceDto> getPlaceById(
            @Parameter(
                    description = "The ID of the place to get",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String id
    ) {
        var place = this.placesService.getPlaceById(id);
        if (place == null) {
            throw new NotFoundException("Place not found");
        }
        return ResponseEntity.ok(place);
    }

    @Operation(
            summary = "Get places",
            description = "Returns a paginated list of all places"
    )
    @GetMapping(value = {"", "/"}, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PaginationDto<PlaceDto>> getAllPlaces(
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
        var places = this.placesService.getAllPlaces(pageable);
        var pagination = PaginationDto.fromEntityModel(
                this.pagedResourcesAssembler.toModel(places)
        );
        return ResponseEntity.ok(pagination);
    }

    @Operation(
            summary = "Get places by an array of IDs",
            description = "Returns a list of places that match the given IDs"
    )
    @GetMapping(value = "/batch", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PlaceDto>> getPlacesByIdArray(
            @Parameter(description = "List of place IDs", required = true)
            @RequestParam List<String> ids
    ) {
        var places = this.placesService.getByIdArray(ids);
        return ResponseEntity.ok(places);
    }

    @Operation(
            summary = "Get random places",
            description = "Returns a list of random places of a specified length"
    )
    @GetMapping(value = "/random/{count}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PlaceDto>> getRandomPlaces(
            @Parameter(
                    description = "The number of random places to get",
                    example = "5"
            )
            @PathVariable
            int count
    ) {
        return ResponseEntity.ok(this.placesService.getRandomPlaces(count));
    }

    @Operation(
            summary = "Search places by generated tags",
            description = "Returns a chat history"
    )
    @PostMapping(
            value = "/search",
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ChatHistoryDto> search(
            @Parameter(
                    description = "Optional conversation identifier for an ongoing chat. If provided, the chat history is updated; " +
                            "if omitted, a new chat history is created.",
                    example = "60f1b3b3b3b3b3b3b3b3b3"
            )
            @RequestParam(required = false)
            String conversationId,
            @Parameter(
                    description = "The search query",
                    required = true,
                    example = "restaurant"
            )
            @RequestBody
            SearchDto body
    ) throws BadRequestException {
        Response<TagsDto> tagsResponse =
                tagsService.generateTags(body.prompt(), 0);
        if (tagsResponse.isRefusal()) {
            throw new BadRequestException(tagsResponse.getRefusal());
        }

        List<TagDto> tags = List.of(tagsResponse.getContent().tags());
        List<PlaceDto> places = placesService.getPlacesByTags(tags);

        // Create messages for the user prompt and AI response.
        Message userMessage = new Message(
                Message.Sender.USER,
                new String[]{body.prompt()}
        );
        Message aiMessage = new Message(
                Message.Sender.AI,
                places.stream()
                        .map(PlaceDto::id)
                        .toArray(String[]::new)
        );

        String authenticatedUserId = SecurityUtils.getAuthenticatedUserId();
        ChatHistoryDto chatHistoryDto = chatHistoryService.addToChatHistory(
                conversationId,
                authenticatedUserId,
                body.prompt(),
                List.of(userMessage, aiMessage)
        );

        return ResponseEntity.ok(chatHistoryDto);
    }

    @Operation(
            summary = "Get nearby places",
            description = "Returns a paginated list of places near the specified coordinates within a given radius"
    )
    @GetMapping(value = "/nearby", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PaginationDto<PlaceDto>> getNearbyPlaces(
            @Parameter(
                    description = "Latitude of the center point",
                    required = true,
                    example = "40.7128"
            )
            @RequestParam
            double latitude,
            @Parameter(
                    description = "Longitude of the center point",
                    required = true,
                    example = "-74.0060"
            )
            @RequestParam
            double longitude,
            @Parameter(
                    description = "Radius in kilometers to search within (default: 10)",
                    example = "10"
            )
            @RequestParam(required = false)
            Double radius,
            @Parameter(
                    description = "The page number to get",
                    example = "0"
            )
            @RequestParam(defaultValue = "0")
            int page,
            @Parameter(
                    description = "The number of items per page",
                    example = "20"
            )
            @RequestParam(defaultValue = "20")
            int size
    ) {
        CoordinateDto coordinateDto = new CoordinateDto(latitude, longitude);
        Pageable pageable = PageRequest.of(page, size);
        var nearbyPlaces = placesService.getNearbyPlaces(coordinateDto, radius, pageable);
        var pagination = PaginationDto.fromEntityModel(
                pagedResourcesAssembler.toModel(nearbyPlaces)
        );
        return ResponseEntity.ok(pagination);
    }

    @Operation(
            summary = "Update a place",
            description = "Accepts a request with a form data to update a place, and returns the updated place"
    )
    @PutMapping(
            value = "/{id}",
            consumes = MULTIPART_FORM_DATA_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PlaceDto> updatePlace(
            @Parameter(
                    description = "The ID of the place to update",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String id,
            @Parameter(description = "The place to update", required = true)
            @ModelAttribute
            CreateUpdatePlaceDto place
    ) {
        PlaceDto updatedPlace = this.placesService.updatePlace(id, place);
        if (updatedPlace == null) {
            throw new NotFoundException("Place not found");
        }
        return ResponseEntity.ok(updatedPlace);
    }

    @Operation(
            summary = "Delete a place",
            description = "Deletes the place with the given ID"
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletePlace(
            @Parameter(
                    description = "The ID of the place to delete",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String id
    ) {
        try {
            this.placesService.deletePlace(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Place not found");
        } catch (Exception e) {
            this.logger.severe(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
