package com.tokorokoshi.tokoro.modules.userRatings;

import com.tokorokoshi.tokoro.modules.userRatings.dto.CreateUpdateUserRatingDto;
import com.tokorokoshi.tokoro.modules.userRatings.dto.UserRatingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/user-ratings")
public class UserRatingsController {
    private final UserRatingsService userRatingsService;

    @Autowired
    public UserRatingsController(UserRatingsService userRatingsService) {
        this.userRatingsService = userRatingsService;
    }

    @PostMapping(value = {"", "/"},
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRatingDto> createUserRating(
        @RequestBody CreateUpdateUserRatingDto userRating
    ) {
        return ResponseEntity.ok(
            this.userRatingsService.saveUserRating(userRating)
        );
    }

    @GetMapping(value = "/{id}",
        produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRatingDto> getUserRating(
        @PathVariable String id
    ) {
        return ResponseEntity.ok(
            this.userRatingsService.findUserRatingById(id)
        );
    }

    @GetMapping(value = {"", "/"},
        produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserRatingDto>> getAllUserRatings() {
        return ResponseEntity.ok(this.userRatingsService.findAllUserRatings());
    }

    @PutMapping(value = "/{id}",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRatingDto> updateUserRating(
        @RequestBody CreateUpdateUserRatingDto userRating,
        @PathVariable String id
    ) {
        UserRatingDto updatedUserRating = this.userRatingsService
            .updateUserRating(id, userRating);
        if (updatedUserRating == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUserRating);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUserRating(@PathVariable String id) {
        this.userRatingsService.deleteUserRating(id);
    }
}
