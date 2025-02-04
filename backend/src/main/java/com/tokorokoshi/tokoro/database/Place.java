package com.tokorokoshi.tokoro.database;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * Represents a place in the database.
 */
@Document(collection = "place")
public record Place(
        @Id
        String id,
        @NonNull
        @Indexed(unique = true)
        String name,
        String description,
        @NonNull
        @Field
        Location location,
        String categoryId,
        @NonNull
        List<HashTag> tags,
        @NonNull
        List<String> pictures,
        @NonNull
        double rating
) {
    /**
     * Creates a new place with the given pictures.
     *
     * @param pictures The list of pictures of the place
     * @return A new place with the given pictures
     */
    public Place withPictures(List<String> pictures) {
        return new Place(
                id,
                name,
                description,
                location,
                categoryId,
                tags,
                pictures,
                rating
        );
    }

    /**
     * Creates a new place with the given tags.
     *
     * @param tags The list of tags of the place
     * @return A new place with the given tags
     */
    public Place withTags(List<HashTag> tags) {
        return new Place(
                id,
                name,
                description,
                location,
                categoryId,
                tags,
                pictures,
                rating
        );
    }
}
