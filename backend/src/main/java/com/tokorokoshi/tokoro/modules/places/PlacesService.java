package com.tokorokoshi.tokoro.modules.places;


import com.tokorokoshi.tokoro.database.HashTag;
import com.tokorokoshi.tokoro.database.Place;
import com.tokorokoshi.tokoro.modules.file.FileStorageService;
import com.tokorokoshi.tokoro.modules.places.dto.CreateUpdatePlaceDto;
import com.tokorokoshi.tokoro.modules.places.dto.PlaceDto;
import com.tokorokoshi.tokoro.modules.tags.TagsService;
import com.tokorokoshi.tokoro.modules.tags.dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
     * Generates tags for a place.
     *
     * @param place Place to generate tags for
     * @return the generated tags
     */
    private List<HashTag> generateTagsForPlace(Place place) {
        Objects.requireNonNull(place, "Place cannot be null");

        var response = tagsService.generateTags(
                "Generate tags for place: " + place
        );
        if (response.isRefusal()) {
            throw new IllegalStateException(
                    "Failed to generate tags: " + response.getRefusal()
            );
        }
        return Arrays.stream(response.getContent().tags())
                .map(tag -> new HashTag(tag.lang(), tag.name()))
                .toList();
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
                key -> fileStorageService.generateSignedUrl(key).join()
        ).toList();
        return placeMapper.toPlaceDto(place.withPictures(picturesUrls));
    }

    /**
     * Saves a new place with optional pictures.
     *
     * @param place place data
     * @return the saved place
     */
    public PlaceDto savePlace(
            CreateUpdatePlaceDto place
    ) {
        // Validate pictures
        if (isFilesInvalid(place.pictures())) {
            throw new IllegalArgumentException("Invalid file type");
        }

        // Map DTO to Place schema
        var placeSchema = placeMapper.toPlaceSchema(place);

        // Process picture files if provided
        List<String> pictureKeys = fileStorageService
                .uploadFiles(
                        place.pictures() != null
                                ? List.of(place.pictures())
                                : List.of(),
                        "places"
                )
                .join();

        // Create a new Place instance with the uploaded picture keys.
        placeSchema = placeSchema.withPictures(pictureKeys);

        // Update tags
        placeSchema = placeSchema.withTags(generateTagsForPlace(placeSchema));

        // Save to MongoDB
        var savedPlace = mongoTemplate.save(placeSchema);
        return getPlaceWithPicturesUrls(savedPlace);
    }

    /**
     * Updates an existing place with optional pictures.
     *
     * @param id    place ID
     * @param place place data
     * @return the updated place
     */
    public PlaceDto updatePlace(
            String id,
            CreateUpdatePlaceDto place
    ) {
        PlaceDto existingPlaceDto = getPlaceById(id);
        if (existingPlaceDto == null) {
            return null;
        }

        // Validate pictures
        if (isFilesInvalid(place.pictures())) {
            throw new IllegalArgumentException("Invalid file type");
        }

        // For update, we assume replacing pictures with new ones.
        // Remove existing pictures from storage if there are any new pictures.
        if (existingPlaceDto.pictures() != null && place.pictures() != null) {
            for (String key : existingPlaceDto.pictures()) {
                fileStorageService.deleteFile(key);
            }
        }

        // Map DTO to Place schema (for update)
        var placeSchema = placeMapper.toPlaceSchema(place);

        // Process new picture files if provided
        List<String> pictureKeys = fileStorageService
                .uploadFiles(
                        place.pictures() != null
                                ? List.of(place.pictures())
                                : List.of(),
                        "places"
                )
                .join();

        // For update, we assume replacing pictures with new ones.
        placeSchema = pictureKeys.isEmpty()
                ? placeSchema.withPictures(placeSchema.pictures())
                : placeSchema.withPictures(pictureKeys);

        // Update tags
        placeSchema = placeSchema.withTags(generateTagsForPlace(placeSchema));

        // Update the place in MongoDB
        var savedPlace = mongoTemplate.save(placeSchema);
        return getPlaceWithPicturesUrls(savedPlace);
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
     * Retrieves paginated places.
     *
     * @param pageable pagination information (page, size)
     * @return paginated list of places
     */
    public Page<PlaceDto> getAllPlaces(Pageable pageable) {
        Query query = new Query().with(pageable);
        List<Place> places = mongoTemplate.find(query, Place.class);
        long total = mongoTemplate.count(new Query(), Place.class); // Total count of all places

        List<PlaceDto> content = places.stream()
                .map(this::getPlaceWithPicturesUrls)
                .toList();
        return new PageImpl<>(content, pageable, total);
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
            fileStorageService.deleteFile(key).join();
        }

        // Remove place from database
        mongoTemplate.remove(place);
    }

    /**
     * Gets random places.
     *
     * @param count number of places to get
     * @return the random places
     */
    public List<PlaceDto> getRandomPlaces(int count) {
        AggregationResults<Place> results = mongoTemplate.aggregate(
                newAggregation(Aggregation.sample(count)),
                Place.class,
                Place.class
        );
        return results.getMappedResults().stream()
                .map(this::getPlaceWithPicturesUrls)
                .toList();
    }

    /**
     * Gets places that have at least one of the specified tags.
     *
     * @param tags tags to search for
     * @return places that contain at least one of the specified tags
     */
    public List<PlaceDto> getPlacesByTags(List<TagDto> tags) {
        if (tags == null || tags.isEmpty()) {
            throw new IllegalArgumentException("Tags cannot be null or empty");
        }

        // For each tag, create an individual criterion and add it to an OR list
        var matchers = tags.stream()
                .map(tag -> Criteria.where("tags")
                        .elemMatch(Criteria.where("name").is(tag.name())))
                .toList();

        // Create criteria to check if the place's tags array contains ANY of the specified tags
        Criteria criteria = new Criteria().orOperator(matchers);
        MatchOperation operation = Aggregation.match(criteria);

        AggregationResults<Place> results = mongoTemplate.aggregate(
                newAggregation(operation),
                Place.class,
                Place.class
        );

        return results.getMappedResults().stream()
                .map(this::getPlaceWithPicturesUrls)
                .toList();
    }
}
