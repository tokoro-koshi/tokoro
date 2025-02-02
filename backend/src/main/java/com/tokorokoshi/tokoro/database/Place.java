package com.tokorokoshi.tokoro.database;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

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
    public Place withId(String id) {
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
