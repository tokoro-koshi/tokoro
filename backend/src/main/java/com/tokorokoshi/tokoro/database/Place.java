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
        List<HashTag> hashTags,
        @NonNull
        double rating
) {
    public Place withId(String id) {
        return new Place(id, name, description, location, categoryId, hashTags, rating);
    }
}
