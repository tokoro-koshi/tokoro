package com.tokorokoshi.tokoro.modules.place;

import com.tokorokoshi.tokoro.modules.place.dto.CreateUpdatePlaceDto;
import com.tokorokoshi.tokoro.modules.place.dto.PlaceDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/place")
public class PlaceController {
    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    //CRUD
    @PostMapping(value = {"", "/"},
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PlaceDto> savePlace(@RequestBody CreateUpdatePlaceDto place) {
        return ResponseEntity.ok(placeService.savePlace(place));
    }

    @GetMapping(value = "/{id}",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PlaceDto> getPlaceById(@PathVariable String id) {
        return ResponseEntity.ok(placeService.getPlaceById(id));
    }

    @GetMapping(value = {"", "/"},
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PlaceDto>> getAllPlaces() {
        return ResponseEntity.ok(placeService.getAllPlaces());
    }

    @PutMapping(value = "/{id}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PlaceDto> updatePlace(@PathVariable String id, @RequestBody CreateUpdatePlaceDto place) {
        PlaceDto updatedPlace = placeService.updatePlace(id, place);
        if (updatedPlace == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPlace);
    }

    @DeleteMapping(value = "/{id}")
    public void deletePlace(@PathVariable String id) {
        placeService.deletePlace(id);
    }
}
