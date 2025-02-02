package com.tokorokoshi.tokoro.modules.places;

import com.tokorokoshi.tokoro.modules.places.dto.CreateUpdatePlaceDto;
import com.tokorokoshi.tokoro.modules.places.dto.PlaceDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/places")
public class PlacesController {
    private final PlacesService placesService;

    public PlacesController(PlacesService placesService) {
        this.placesService = placesService;
    }

    @PostMapping(value = {"", "/"},
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PlaceDto> savePlace(
        @RequestPart("data")
        CreateUpdatePlaceDto place,
        @RequestPart(value = "pictures", required = false)
        MultipartFile[] pictures
    ) {
        return ResponseEntity.ok(placesService.savePlace(place, pictures));
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PlaceDto> getPlaceById(@PathVariable String id) {
        return ResponseEntity.ok(placesService.getPlaceById(id));
    }

    @GetMapping(value = {"", "/"}, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PlaceDto>> getAllPlaces() {
        return ResponseEntity.ok(placesService.getAllPlaces());
    }

    @PutMapping(value = "/{id}",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PlaceDto> updatePlace(
        @PathVariable String id,
        @RequestPart("data")
        CreateUpdatePlaceDto place,
        @RequestPart(value = "pictures", required = false)
        MultipartFile[] pictures
    ) {
        PlaceDto updatedPlace = placesService.updatePlace(id, place, pictures);
        if (updatedPlace == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPlace);
    }

    @DeleteMapping(value = "/{id}")
    public void deletePlace(@PathVariable String id) {
        placesService.deletePlace(id);
    }
}
