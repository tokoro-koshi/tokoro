package com.tokorokoshi.tokoro.database;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "user")
public record User(
        // _id is reserved MongoDB name for primary key with ObjectID type (conforms to string)
        // @Id annotation is used to mark the field as the primary key and bind _id to it
        @Id
        String id,
        @Indexed(unique = true)
        @NonNull
        String username,
        @Indexed(unique = true)
        @NonNull
        String email,
        @NonNull
        String password,
        @Field("preferences")
        UserPreferences preferences,
        @Field("favorites")
        UserFavorites favorites
) {
        public User withId(String id) {
                return new User(id, username, email, password, preferences, favorites);
        }
}
