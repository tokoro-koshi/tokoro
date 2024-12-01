package com.tokorokoshi.tokoro.database.schema;

import com.mongodb.lang.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class UserFavorites {
    @DBRef
    private List<ObjectId> places;

    @DBRef
    private List<ObjectId> prompts;

    public UserFavorites() {
        this.places = List.of();
        this.prompts = List.of();
    }

    @NonNull
    public List<ObjectId> getPlaces() {
        return places;
    }

    public void setPlaces(@NonNull List<ObjectId> places) {
        this.places = places;
    }

    @NonNull
    public List<ObjectId> getPrompts() {
        return prompts;
    }

    public void setPrompts(@NonNull List<ObjectId> prompts) {
        this.prompts = prompts;
    }
}
