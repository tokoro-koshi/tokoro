package com.tokorokoshi.tokoro.modules.userRating;

import com.tokorokoshi.tokoro.modules.userRating.dto.CreateUpdateUserRatingDto;
import com.tokorokoshi.tokoro.modules.userRating.dto.UserRatingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/user-rating")
public class UserRatingController {
    private final UserRatingService userRatingService;

    @Autowired
    public UserRatingController(UserRatingService userRatingService) {
        this.userRatingService = userRatingService;
    }

    //Crud
    @PostMapping(value = {"", "/"},
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRatingDto> createUserRating(
            @RequestBody CreateUpdateUserRatingDto userRating) {
        return ResponseEntity.ok(this.userRatingService.saveUserRating(userRating));
    }

    @GetMapping(value = "/{id}",
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRatingDto> getUserRating(@PathVariable String id) {
        return ResponseEntity.ok(this.userRatingService.findUserRatingById(id));
    }

    @GetMapping(value = {"/all", "list"},
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserRatingDto>> getAllUserRatings() {
        return ResponseEntity.ok(this.userRatingService.findAllUserRatings());
    }

    @PutMapping(value = "/{id}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRatingDto> updateUserRating(
            @RequestBody CreateUpdateUserRatingDto userRating,
            @PathVariable String id) {
        return ResponseEntity.ok(this.userRatingService.updateUserRating(id, userRating));
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUserRating(@PathVariable String id) {
        this.userRatingService.deleteUserRating(id);
    }
}
