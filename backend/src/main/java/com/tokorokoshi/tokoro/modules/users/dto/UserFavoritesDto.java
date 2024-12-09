package com.tokorokoshi.tokoro.modules.users.dto;

import java.util.List;

public class UserFavoritesDto {
    private List<String> places;
    private List<String> prompts;

    public UserFavoritesDto(){
        this.places = List.of();
        this.prompts = List.of();
    }

    public List<String> getPlaces() {
        return places;
    }

    public void setPlaces(List<String> places) {
        this.places = places;
    }

    public List<String> getPrompts() {
        return prompts;
    }

    public void setPrompts(List<String> prompts) {
        this.prompts = prompts;
    }
}
