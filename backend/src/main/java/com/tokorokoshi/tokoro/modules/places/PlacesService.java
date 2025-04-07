package com.tokorokoshi.tokoro.modules.places;

import com.tokorokoshi.tokoro.database.HashTag;
import com.tokorokoshi.tokoro.database.Place;
import com.tokorokoshi.tokoro.modules.file.FileStorageService;
import com.tokorokoshi.tokoro.modules.places.dto.CoordinateDto;
import com.tokorokoshi.tokoro.modules.places.dto.CreateUpdatePlaceDto;
import com.tokorokoshi.tokoro.modules.places.dto.PlaceDto;
import com.tokorokoshi.tokoro.modules.tags.TagsService;
import com.tokorokoshi.tokoro.modules.tags.dto.TagDto;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Service
public class PlacesService {
    private final MongoTemplate repository;
    private final PlaceMapper placeMapper;
    private final FileStorageService fileStorageService;
    private final TagsService tagsService;

    @Autowired
    public PlacesService(
            MongoTemplate repository,
            PlaceMapper placeMapper,
            FileStorageService fileStorageService,
            TagsService tagsService
    ) {
        this.repository = repository;
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
        var savedPlace = repository.save(placeSchema);
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
        Place existingPlace = repository.findById(id, Place.class);
        if (place == null) return null;

        // Validate pictures
        if (isFilesInvalid(place.pictures())) {
            throw new IllegalArgumentException("Invalid file type");
        }

        // For update, we assume replacing pictures with new ones.
        // Remove existing pictures from storage if there are any new pictures.
        if (!existingPlace.pictures().isEmpty() && place.pictures() != null) {
            for (String key : existingPlace.pictures()) {
                fileStorageService.deleteFile(key);
            }
        }

        // Map DTO to Place schema (for update)
        var placeSchema = placeMapper.toPlaceSchema(place).withId(id);

        // Process new picture files if provided
        List<String> pictureKeys = place.pictures() == null
            ? existingPlace.pictures()
            : fileStorageService
                .uploadFiles(List.of(place.pictures()), "places")
                .join();

        // For update, we assume replacing pictures with new ones.
        placeSchema = pictureKeys.isEmpty()
                ? placeSchema.withPictures(placeSchema.pictures())
                : placeSchema.withPictures(pictureKeys);

        // Update tags
        placeSchema = placeSchema.withTags(generateTagsForPlace(placeSchema));

        // Update the place in MongoDB
        var savedPlace = repository.save(placeSchema);
        return getPlaceWithPicturesUrls(savedPlace);
    }

    /**
     * Retrieves a place by ID.
     *
     * @param id place ID
     * @return the place
     */
    public PlaceDto getPlaceById(String id) {
        var place = repository.findById(id, Place.class);
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
        List<Place> places = repository.find(query, Place.class);
        long total = repository.count(new Query(), Place.class); // Total count of all places

        List<PlaceDto> content = places.stream()
                .map(this::getPlaceWithPicturesUrls)
                .toList();
        return new PageImpl<>(content, pageable, total);
    }

    /**
     * Retrieves places by an array of IDs using an aggregation query.
     *
     * @param ids List of place IDs
     * @return List of places
     */
    public List<PlaceDto> getByIdArray(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }

        MatchOperation matchStage = Aggregation.match(Criteria.where("_id").in(ids));
        Aggregation aggregation = Aggregation.newAggregation(matchStage);
        AggregationResults<Place> results = repository.aggregate(aggregation, Place.class, Place.class);

        return results.getMappedResults().stream()
                .map(this::getPlaceWithPicturesUrls)
                .toList();
    }

    /**
     * Deletes a place by ID.
     *
     * @param id place ID
     */
    public void deletePlace(String id) {
        var place = repository.findById(id, Place.class);
        if (place == null) {
            throw new IllegalArgumentException("Place not found for id: " + id);
        }

        // Remove files from storage
        for (String key : place.pictures()) {
            fileStorageService.deleteFile(key).join();
        }

        // Remove place from database
        repository.remove(place);
    }

    /**
     * Gets random places.
     *
     * @param count number of places to get
     * @return the random places
     */
    public List<PlaceDto> getRandomPlaces(int count) {
        AggregationResults<Place> results = repository.aggregate(
                newAggregation(Aggregation.sample(count)),
                Place.class,
                Place.class
        );
        return results.getMappedResults().stream()
                .map(this::getPlaceWithPicturesUrls)
                .toList();
    }

    /**
     * Retrieves places near a given coordinate within a specified radius with pagination.
     *
     * @param coordinateDto The coordinate to search around
     * @param radius        The radius in kilometers (default to 10 if not provided)
     * @param pageable      Pagination information (page, size, sort)
     * @return Paginated list of nearby places sorted by distance
     */
    public Page<PlaceDto> getNearbyPlaces(CoordinateDto coordinateDto, Double radius, Pageable pageable) {
        // Set default radius if not provided
        double searchRadius = radius != null ? radius : 10.0;

        // Convert kilometers to meters (MongoDB uses meters in geo-queries)
        double searchRadiusMeters = searchRadius * 1000;

        GeoJsonPoint center = new GeoJsonPoint(coordinateDto.longitude(), coordinateDto.latitude());

        // Create query with geospatial criteria and pagination
        Criteria locationCriteria = Criteria
                .where("location.coordinate")
                .nearSphere(center)
                .maxDistance(searchRadiusMeters);

        Query query = Query
                .query(locationCriteria)
                .with(pageable);

        // Execute the query and get results
        List<Place> places = repository.find(query, Place.class);

        // Get total count within radius
        long total = repository.count(Query.query(locationCriteria), Place.class);

        // Map to PlaceDto with picture URLs
        List<PlaceDto> content = places.stream()
                .map(this::getPlaceWithPicturesUrls)
                .toList();

        return new PageImpl<>(content, pageable, total);
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

        // Extract tag names from the input DTOs
        List<String> tagNames = tags.stream()
                .map(TagDto::name)
                .toList();

        // Create OR criteria for matching any of the tags
        List<Criteria> matchers = tags.stream()
                .map(tag -> Criteria.where("tags")
                        .elemMatch(Criteria.where("name").is(tag.name())))
                .toList();
        Criteria criteria = new Criteria().orOperator(matchers);
        MatchOperation matchStage = Aggregation.match(criteria);

        // Add a field representing the count of matching tags
        AggregationOperation addHitsStage = context -> new Document("$addFields",
                new Document("hits",
                        new Document("$size",
                                new Document("$filter",
                                        new Document("input", "$tags")
                                                .append("as", "tag")
                                                .append("cond",
                                                        new Document("$in", List.of("$$tag.name", tagNames))
                                                )
                                )
                        )
                )
        );

        // Sort by the hits in descending order
        SortOperation sortStage = Aggregation.sort(Sort.by(Sort.Direction.DESC, "hits"));

        // Build the aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(
                matchStage,
                addHitsStage,
                sortStage
        );

        // Execute the aggregation
        AggregationResults<Place> results = repository.aggregate(
                aggregation,
                Place.class,
                Place.class
        );

        return results.getMappedResults().stream()
                .map(this::getPlaceWithPicturesUrls)
                .toList();
    }
}
