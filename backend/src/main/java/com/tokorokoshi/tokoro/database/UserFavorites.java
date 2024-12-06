package com.tokorokoshi.tokoro.database;

import com.mongodb.lang.NonNull;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


public class UserFavorites {
    private List<String> places;
    private List<String> prompts;

    public UserFavorites() {
        this.places = List.of();
        this.prompts = List.of();
    }

    @NonNull
    public List<String> getPlaces() {
        return places;
    }

    public void setPlaces(@NonNull List<String> places) {
        this.places = places;
    }

    @NonNull
    public List<String> getPrompts() {
        return prompts;
    }

    public void setPrompts(@NonNull List<String> prompts) {
        this.prompts = prompts;
    }
}
