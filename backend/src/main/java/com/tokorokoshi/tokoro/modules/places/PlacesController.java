package com.tokorokoshi.tokoro.modules.places;

import com.tokorokoshi.tokoro.modules.places.dto.CreateUpdatePlaceDto;
import com.tokorokoshi.tokoro.modules.places.dto.PlaceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Tag(name = "Places", description = "API for managing places")
@RestController
@RequestMapping("/api/places")
public class PlacesController {
    private final PlacesService placesService;

    public PlacesController(PlacesService placesService) {
        this.placesService = placesService;
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
        return ResponseEntity.ok(this.placesService.getPlaceById(id));
    }

    @Operation(
            summary = "Get all places",
            description = "Returns a paginated list of all places"
    )
    @GetMapping(value = {"", "/"}, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PlaceDto>> getAllPlaces(
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
        return ResponseEntity.ok(this.placesService.getAllPlaces(pageable));
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
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPlace);
    }

    @Operation(
            summary = "Delete a place",
            description = "Deletes the place with the given ID"
    )
    @DeleteMapping(value = "/{id}")
    public void deletePlace(
            @Parameter(
                    description = "The ID of the place to delete",
                    required = true,
                    example = "60f1b3b3b3b3b3b3b3b3b3"
            )
            @PathVariable
            String id
    ) {
        this.placesService.deletePlace(id);
    }
}
