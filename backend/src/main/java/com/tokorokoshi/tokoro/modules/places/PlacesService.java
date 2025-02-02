package com.tokorokoshi.tokoro.modules.places;

import com.tokorokoshi.tokoro.database.Place;
import com.tokorokoshi.tokoro.modules.file.FileStorageService;
import com.tokorokoshi.tokoro.modules.places.dto.CreateUpdatePlaceDto;
import com.tokorokoshi.tokoro.modules.places.dto.PlaceDto;
import com.tokorokoshi.tokoro.modules.tags.TagsService;
import com.tokorokoshi.tokoro.modules.tags.dto.TagsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Service
public class PlacesService {
    private final MongoTemplate mongoTemplate;
    private final PlaceMapper placeMapper;
    private final FileStorageService fileStorageService;
    private final TagsService tagsService;

    @Autowired
    public PlacesService(
        MongoTemplate mongoTemplate,
        PlaceMapper placeMapper,
        FileStorageService fileStorageService,
        TagsService tagsService
    ) {
        this.mongoTemplate = mongoTemplate;
        this.placeMapper = placeMapper;
        this.fileStorageService = fileStorageService;
        this.tagsService = tagsService;
    }

    /**
     * Validates the files to be images.
     *
     * @param files files to validate
     * @return true if any file is invalid, false otherwise
     */
    private boolean isFilesInvalid(MultipartFile[] files) {
        if (files == null) {
            return false;
        }
        for (MultipartFile file : files) {
            String mimeType = file.getContentType();
            if (mimeType == null || !mimeType.startsWith("image/")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a place with pictures URLs.
     */
    private PlaceDto getPlaceWithPicturesUrls(Place place) {
        List<String> picturesUrls = place.pictures().stream().map(
            key -> fileStorageService.generateSignedUrl(key, 3600).join()
        ).toList();
        return placeMapper.toPlaceDto(place.withPictures(picturesUrls));
    }

    /**
     * Saves a new place with optional pictures.
     *
     * @param place    place data
     * @param pictures optional pictures
     * @return the saved place
     */
    public PlaceDto savePlace(
        CreateUpdatePlaceDto place,
        MultipartFile[] pictures
    ) {
        // Validate pictures
        if (isFilesInvalid(pictures)) {
            throw new IllegalArgumentException("Invalid file type");
        }

        // Map DTO to Place schema
        var placeSchema = placeMapper.toPlaceSchema(place);

        // Process picture files if provided
        List<String> pictureKeys = fileStorageService
            .uploadFiles(List.of(pictures), "places")
            .join();

        // Create a new Place instance with the uploaded picture keys.
        placeSchema = placeSchema.withPictures(pictureKeys);

        // Save to MongoDB
        var savedPlace = mongoTemplate.save(placeSchema);
        return placeMapper.toPlaceDto(savedPlace);
    }

    /**
     * Updates an existing place with optional pictures.
     *
     * @param id       place ID
     * @param place    place data
     * @param pictures optional pictures
     * @return the updated place
     */
    public PlaceDto updatePlace(
        String id,
        CreateUpdatePlaceDto place,
        MultipartFile[] pictures
    ) {
        PlaceDto existingPlaceDto = getPlaceById(id);
        if (existingPlaceDto == null) {
            return null;
        }

        // Validate pictures
        if (isFilesInvalid(pictures)) {
            throw new IllegalArgumentException("Invalid file type");
        }

        // For update, we assume replacing pictures with new ones.
        // Remove existing pictures from storage if there are any new pictures.
        if (existingPlaceDto.pictures() != null && pictures != null) {
            for (String key : existingPlaceDto.pictures()) {
                fileStorageService.deleteFile(key);
            }
        }

        // Map DTO to Place schema (for update)
        var placeSchema = placeMapper.toPlaceSchema(place);

        // Process new picture files if provided
        List<String> pictureKeys = fileStorageService
            .uploadFiles(List.of(pictures), "places")
            .join();

        // For update, we assume replacing pictures with new ones.
        placeSchema = pictureKeys.isEmpty()
            ? placeSchema.withPictures(placeSchema.pictures())
            : placeSchema.withPictures(pictureKeys);
        var savedPlace = mongoTemplate.save(placeSchema);
        return placeMapper.toPlaceDto(savedPlace);
    }

    /**
     * Retrieves a place by ID.
     *
     * @param id place ID
     * @return the place
     */
    public PlaceDto getPlaceById(String id) {
        var place = mongoTemplate.findById(id, Place.class);
        if (place == null) return null;
        return getPlaceWithPicturesUrls(place);
    }

    /**
     * Retrieves all places.
     *
     * @return all places
     */
    public List<PlaceDto> getAllPlaces() {
        return mongoTemplate.findAll(Place.class)
                            .stream()
                            .map(this::getPlaceWithPicturesUrls)
                            .toList();
    }

    /**
     * Deletes a place by ID.
     *
     * @param id place ID
     */
    public void deletePlace(String id) {
        var place = mongoTemplate.findById(id, Place.class);
        if (place == null) {
            throw new IllegalArgumentException("Place not found for id: " + id);
        }

        // Remove files from storage
        for (String key : place.pictures()) {
            fileStorageService.deleteFile(key);
        }

        // Remove place from database
        mongoTemplate.remove(place);
    }

    /**
     * Generates tags for a place.
     *
     * @param id place ID
     * @return the generated tags
     */
    private TagsDto generateTagsForPlace(String id) {
        var place = mongoTemplate.findById(id, Place.class);
        if (place == null) {
            throw new IllegalArgumentException("Place not found for id: " + id);
        }

        var response = tagsService.generateTags(
            "Generate tags for place: " + place
        );
        if (response.isRefusal()) {
            throw new IllegalStateException(
                "Failed to generate tags: " + response.getRefusal()
            );
        }
        return response.getContent();
    }
}
